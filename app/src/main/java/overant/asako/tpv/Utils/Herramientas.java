package overant.asako.tpv.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import overant.asako.tpv.R;


public class Herramientas {

    public static class ponerImagen extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public ponerImagen(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null)
            imageView.setImageBitmap(result);
        }
    }

    public void mensaje(String msg, Activity act) {
        Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
    }


}
