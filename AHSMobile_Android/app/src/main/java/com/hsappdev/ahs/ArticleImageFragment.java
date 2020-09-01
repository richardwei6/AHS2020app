package com.hsappdev.ahs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hsappdev.ahs.misc.Helper;

/**
 * A simple Image holder compatible with FragmentPagerAdapters solely for use in viewPagers in articles
 */
public class ArticleImageFragment extends Fragment implements View.OnClickListener{

    private static final String imagePath_KEY = "1";
    private String imagePath;
    public static ArticleImageFragment newInstance(String imagePath) {
        ArticleImageFragment thisFrag = new ArticleImageFragment();
        Bundle args = new Bundle();
        args.putString(imagePath_KEY, imagePath);
        thisFrag.setArguments(args);
        return thisFrag;
    }

    private OnImageClick onImageClick;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onImageClick = (OnImageClick) context;
        }
        catch(ClassCastException e) {
            throw new ClassCastException("Context failed to implement onImageClick interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            imagePath = getArguments().getString(imagePath_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article_image, container, false);
        /*if(container == null) // apparently, it always is in the viewpager
            Log.d(TAG, "help, the container is null");*/
        ImageView imageView = view.findViewById(R.id.article_image);

        /*container.addView(imageView);*/
        final ImageView open_in_new = view.findViewById(R.id.article_image_open_in_new);
        view.setOnClickListener(this);
        Helper.setImageFromUrl_CenterCrop_FullSize(imageView, imagePath);
        /*Helper.setImageFromUrl(imageView, imagePath, new Helper.OnPaletteLoad() {
            @Override
            public void onLoad(Palette palette) {
                int mutedColor = palette.getMutedColor(ContextCompat.getColor(view.getContext(), R.color.CoffeeRed_473D3D));
                Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.article_image_circle_bg);
                DrawableCompat.setTint(drawable, mutedColor);
                open_in_new.setBackground(drawable);
            }
        });*/
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
                                    int mutedColor = palette.getDarkMutedColor(ContextCompat.getColor(view.getContext(), R.color.CoffeeRed_473D3D));
                                    Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.article_image_circle_bg);
                                    DrawableCompat.setTint(drawable, mutedColor);
                                    open_in_new.setBackground(drawable);
                                }
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        return view;
    }

    @Override
    public void onClick(View v) {
        onImageClick.onClick(imagePath);
    }

    public interface OnImageClick {
        void onClick(String imagePath);
    }
}
