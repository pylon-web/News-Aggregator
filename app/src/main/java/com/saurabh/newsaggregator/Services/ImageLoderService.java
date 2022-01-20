        package com.saurabh.newsaggregator.Services;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.ImageView;

        import com.saurabh.newsaggregator.MainActivity;

        import java.io.InputStream;

        public class ImageLoderService extends AsyncTask<String, Void, Bitmap> {
                ImageView imageView;
                MainActivity  mainActivity;

        public ImageLoderService(ImageView imageView, MainActivity mainActivity) {
                this.imageView = imageView;
                this.mainActivity = mainActivity;
        }
                @Override
        protected Bitmap doInBackground(String... strings) {
                Bitmap Icon = null;
                try {
                Icon = BitmapFactory.decodeStream(new java.net.URL(strings[0]).openStream());
                } catch (Exception e) {
                Log.e("Error", e.getMessage());//Error
                e.printStackTrace();
                }
                return Icon;
                }

        protected void onPostExecute(Bitmap result) {

                if (result == null) {
                        imageView.setImageResource(mainActivity.getResources().getIdentifier("brokenimage", "drawable", mainActivity.getPackageName()));
                } else {
                        imageView.setImageBitmap(result);
                }
                }
                }