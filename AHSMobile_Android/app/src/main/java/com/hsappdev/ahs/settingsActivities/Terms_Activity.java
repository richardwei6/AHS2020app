package com.hsappdev.ahs.settingsActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.misc.FullScreenActivity;
import com.hsappdev.ahs.misc.Helper;

public class Terms_Activity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_terms);

        TextView terms = findViewById(R.id.terms_text);
        /*Helper.setHtmlParsedText_toView(terms, getResources().getString(R.string.terms));*/
        /*terms.setText(getResources().getString(R.string.terms));*/
        Helper.setHtmlParsedText_toView(terms, getResources().getString(R.string.terms));
        /*setTextViewHTML(terms, getResources().getString(R.string.terms));
*/
        ImageView backButton = findViewById(R.id.terms_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /*protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {

        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(@NonNull View view) {
                // Do something with span.getURL() to handle the link click...
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
                startActivity(browserIntent);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }*/
}


