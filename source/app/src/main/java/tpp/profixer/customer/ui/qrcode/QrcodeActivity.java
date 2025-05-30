package tpp.profixer.customer.ui.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.ApiModelUtils;
import tpp.profixer.customer.data.model.api.request.BankInfo;
import tpp.profixer.customer.data.model.api.response.DeepLink;
import tpp.profixer.customer.data.model.api.response.PaymentInfo;
import tpp.profixer.customer.databinding.ActivityQrcodeBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.dialog.BankDialog;
import tpp.profixer.customer.ui.qrcode.adapter.TransactionAdapter;

public class QrcodeActivity extends BaseActivity<ActivityQrcodeBinding, QrcodeViewModel> {

    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;
    public CompositeDisposable compositeDisposableA = new CompositeDisposable();
    private TransactionAdapter transactionAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayoutTransaction();

        viewModel.paymentInfo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                viewModel.showLoading();
                PaymentInfo paymentInfo = viewModel.paymentInfo.get();
                BankInfo request = new BankInfo();
                request.setAccountNo(paymentInfo.getData().getAccountNumber());
                request.setAccountName(paymentInfo.getData().getAccountName());
                request.setAmount(String.valueOf(paymentInfo.getData().getAmount()));
                request.setAddInfo(paymentInfo.getData().getDescription());
                viewModel.generateQrcode(request);
//                Long expiredAt = Long.valueOf(paymentInfo.getData().getExpiredAt());
//                Long currentAt = System.currentTimeMillis();
//                Timber.e(String.valueOf(expiredAt*1000 - currentAt));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long expiredAt = Long.parseLong(paymentInfo.getData().getExpiredAt());
                        long currentAt = System.currentTimeMillis();
                        int time = (int) ((expiredAt*1000 - currentAt)/1000);
                        int minute = time/60;
                        int second = time%60;
                        viewModel.timeString.set(String.format("%02d:%02d", minute, second));
                        if(time > 0){
                            handler.postDelayed(this,1000);
                        }
                    }
                },1000);

                loopCheck(paymentInfo.getData().getPaymentLinkId());
//                try {
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    BitMatrix bitMatrix = barcodeEncoder.encode(viewModel.paymentInfo.get().getData().getQrCode(), BarcodeFormat.QR_CODE, 700, 700);
//                    Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.RGB_565);
//
//                    for (int x = 0; x < bitMatrix.getWidth(); x++) {
//                        for (int y = 0; y < bitMatrix.getHeight(); y++) {
//                            bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                        }
//                    }
//                    viewBinding.imageView.setImageBitmap(bitmap);
//                } catch (WriterException e) {
//                    Timber.e(e);
//                }
            }
        });

        viewModel.status.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.status.get().equals("PAID")){
                    viewModel.getBooking();
                }
            }
        });

        viewModel.booking.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                transactionAdapter.setData(viewModel.booking.get().getTransactions());
            }
        });

        viewModel.paymentInfo.set(ApiModelUtils.fromJson(getIntent().getStringExtra("payment_info"), PaymentInfo.class));

    }

    public void downloadImage(){
        bitmapDrawable= (BitmapDrawable) viewBinding.imageView.getDrawable();
        bitmap= bitmapDrawable.getBitmap() ;
        FileOutputStream fileOutputStream = null;
        File sdCard = Environment.getExternalStorageDirectory() ;
        File Directory=new File(sdCard.getAbsoluteFile()+"/Download");
        Directory.mkdir() ;
        String filename = String.format("%d.jpg",System.currentTimeMillis());
        File outfile = new File(Directory, filename);
        viewModel.showSuccessMessage("Tải xuống mã QR thành công");

        try {
            fileOutputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loopCheck(String id){
        compositeDisposableA.add(io.reactivex.rxjava3.core.Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> viewModel.checkStatus(id))
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
//        compositeDisposableA.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void bankDialog(){
        BankDialog bankDialog = new BankDialog(this, viewModel.deepLinks.get());
        bankDialog.setOnItemBankListener(new BankDialog.OnItemBankListener() {
            @Override
            public void itemClick(DeepLink DeepLink) {
                viewModel.cDeepLink.set(DeepLink);
            }

            @Override
            public void hideKeyboard() {
                hideKeyboard();
            }
        });
        bankDialog.show();
    }

    public void openApp(){
        if(viewModel.cDeepLink.get() == null){
            return;
        }
        String deeplink = viewModel.cDeepLink.get().getDeeplink();
//        if(viewModel.cDeepLink.get().getAutofill() == 1){
            deeplink = deeplink + "&mb=" + viewModel.paymentInfo.get().getData().getAccountNumber() + "@bidv&am=" + viewModel.paymentInfo.get().getData().getAmount() + "&tn=" + viewModel.paymentInfo.get().getData().getDescription();
//        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setLayoutTransaction(){
        transactionAdapter = new TransactionAdapter(this, new ArrayList<>());
        viewBinding.rvTransaction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        viewBinding.rvTransaction.setAdapter(transactionAdapter);
    }
}
