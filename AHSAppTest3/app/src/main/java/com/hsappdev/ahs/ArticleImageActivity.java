package com.hsappdev.ahs;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.hsappdev.ahs.Misc.FullScreenActivity;

public class ArticleImageActivity extends FullScreenActivity {

    public static final String imagePath_key = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_image_layout);

        ImageView backBtn = findViewById(R.id.article_image_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String imagePath = getIntent().getStringExtra(imagePath_key);
        PhotoView photoView = findViewById(R.id.photoview);
        Glide.with(this)
                .load(imagePath)
                .error(R.drawable.image_bg)
                .into(photoView);

        final ViewGroup outerLayout = findViewById(R.id.article_image_outerLayout);
        final ImageView circle_bg = findViewById(R.id.article_image_circlebg);
        Glide
                .with(this)
                .asBitmap()
                .apply(new RequestOptions().override(10,10))
                .load(imagePath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener()
                        {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                if (palette != null) {
                                    int mutedColor = palette.getDarkMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.CoffeeRed_473D3D));
                                    outerLayout.setBackgroundColor(mutedColor);
                                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.article_image_circle_bg);
                                    DrawableCompat.setTint(drawable, mutedColor);
                                    circle_bg.setBackground(drawable);
                                }
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}