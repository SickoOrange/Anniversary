package com.berber.orange.memories.activity.about;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.berber.orange.memories.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        FrameLayout frameLayout = findViewById(R.id.about);
        AboutBuilder builder = AboutBuilder.with(this)
                .setAppIcon(R.drawable.icon)
                .setAppName(R.string.app_name)
                .setPhoto(R.drawable.author_photo)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(5)
                .setName("Ya Yin")
                .setSubTitle("Software Developer")
                .setLinksColumnsCount(3)
                .setBrief("This App is a christmas present for my sweetheart, YaLing Kuo")
                .addGooglePlayStoreLink("8002078663318221363")
                .addEmailLink("heylbly@gmail.com")
                .addWhatsappLink("Ya Yin", "+4917630141050")
                .addFiveStarsAction()
                //.addMoreFromMeAction("Vansuita")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
               // .addUpdateAction()
                .setActionsColumnsCount(3)
                //.addFeedbackAction("heylbly@gmail.com")
                .addPrivacyPolicyAction("http://sickoorange.blogspot.de/")
               // .addIntroduceAction((Intent) null)
              //  .addHelpAction((Intent) null)
              //  .addChangeLogAction((Intent) null)
                .setWrapScrollView(true)
                .setShowAsCard(true);

        AboutView view = builder.build();
        frameLayout.addView(view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
