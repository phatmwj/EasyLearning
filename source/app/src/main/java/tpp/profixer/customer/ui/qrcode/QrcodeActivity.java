package tpp.profixer.customer.ui.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityQrcodeBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;

public class QrcodeActivity extends BaseActivity<ActivityQrcodeBinding, QrcodeViewModel> {

    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;
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

        viewModel.paymentInfo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
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

        viewModel.createBooking();

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
        Toast.makeText( this,"Image Saved Successfully",Toast.LENGTH_SHORT).show() ;

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
}
