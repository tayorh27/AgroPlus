package com.ap.agroplus.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ap.agroplus.R;
import com.hardsoftstudio.real.textview.views.RealTextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PolicyActivity extends AppCompatActivity {

    RealTextView tv;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        tv = (RealTextView) findViewById(R.id.tvPolicy);
        tv.setHtmlFromString("<h2 style=\"text-align: center;\"><strong><u>Description of Service and content policy</u></strong></h2>\n" +
                "<strong>AgroPlus</strong> is not in any way involved in any form transaction between Users. As a result, and as discussed in more detail in these Terms, you hereby acknowledge and agree that AgroPlus is not a party to such transactions, has no control over any element of such transactions, and shall have no liability towards any party in connection with such transactions. You use the App and it’s service at your sole risk and responsibility.\n" +
                "\n" +
                "&nbsp;\n" +
                "<ol>\n" +
                " \t<li><strong>AgroPlus</strong> is not responsible for ads, directory information, business listings/information, messages between users, including without limitation e-mails or chats or other means of electronic communication, whether through the App or another Third Party App/Website or offerings, comments, user postings, files, images, photos, video, sounds, business listings/information and directory information or any other material made available through the App and the Service , and that by using the App and the Service, you may be exposed to Content that is offensive, indecent, inaccurate, misleading, or otherwise objectionable. You acknowledge and agree that you are responsible for and must evaluate, and bear all risks associated with, the use of any Content, that you may not rely on said Content, and that under no circumstances will <strong>AgroPlus</strong> be liable in any way for the Content or for any loss or damage of any kind incurred as a result of the browsing, using or reading any Content listed, e-mailed or otherwise made available via the Service. You acknowledge and agree that <strong>AgroPlus </strong>is not obliged to pre-screen or approve any Content, but that <strong>AgroPlus</strong> has the right, in its sole and absolute discretion, to refuse, delete or move any Content that is or may be available through the Service, for violating these Terms and such violation being brought to <strong>AgroPlus</strong> knowledge or for any other reason or no reason at all. Furthermore, the App and Content available through the App may contain links to other third party websites/Apps ,which are completely unrelated to <strong>AgroPlus</strong>. If you link to Third Party App/Websites, you may be subject to those Third Party Websites/App terms and conditions and other policies. <strong>AgroPlus </strong> makes no representation or guarantee as to the accuracy or authenticity of the information contained in any such Third Party Websites, and your linking to any other websites is completely at your own risk and <strong>AgroPlus</strong> disclaims all liability thereto.</li>\n" +
                " \t<li>You acknowledge and agree that you are solely responsible for your own Content posted on, transmitted through, or linked from the Service and the consequences of posting, transmitting, linking or publishing it. More specifically, you are solely responsible for all Content that you upload, email or otherwise make available via the Service. In connection with such Content posted on, transmitted through, or linked from the Service by you, you affirm, acknowledge, represent, warrant and covenant that: (i) you own or have and shall continue to, for such time the Content is available on the App, have the necessary licenses, rights, consents, and permissions to use such Content on the Service and App (including without limitation all patent, trademark, trade secret, copyright or other proprietary rights in and to any and all such Content) and authorize <strong>AgroPlus</strong> to use such Content to enable inclusion and use of the Content in the manner contemplated by the Service, the App and these Terms; and (ii) you have the written consent, release, and/or permission of each and every identifiable individual person or business in the Content to use the name or likeness of each and every such identifiable individual person or business to enable inclusion and use of the Content in the manner contemplated by the Service, the App and these Terms. For clarity, you retain all of your ownership rights in your Content; however, by submitting any Content on the App, you hereby grant to <strong>AgroPlus</strong> an irrevocable, non-cancellable, perpetual, worldwide, non-exclusive, royalty-free, sub-licensable, transferable license to use, reproduce, distribute, prepare derivative works of, display, and perform the Content in connection with the App including without limitation for the purpose of promoting and redistributing part or all of the App and Content therein in any media formats and through any media channels now or hereafter known. These rights are required by <strong>AgroPlus</strong> in order to host and display your Content. Furthermore, by you posting Content to any public area of the Service, you agree to and do hereby grant to <strong>AgroPlus</strong> all rights necessary to prohibit or allow any subsequent aggregation, display, copying, duplication, reproduction, or exploitation of the Content on the Service by any party for any purpose. You also hereby grant each user of the App a non-exclusive license to access your Content through the App (this license excludes scrapping cases and any other possible use with commercial purposes). The foregoing license to each user granted by you terminates once you or <strong>AgroPlus</strong> remove or delete such Content from the App.</li>\n" +
                " \t<li><strong>AgroPlus</strong> does not endorse any Content or any opinion, statement, recommendation, or advice expressed therein, and <strong>AgroPlus</strong> expressly disclaims any and all liability in connection with user Content. <strong>AgroPlus </strong> does not permit copyright infringing activities and infringement of intellectual property rights on the App, and <strong>AgroPlus</strong> may, at its sole discretion, remove any infringing Content. <strong>AgroPlus</strong> reserves the right to remove any Content without prior notice. <strong>AgroPlus</strong> may also terminate a user's access to the App, if they are determined to be a repeat infringer or found to be indulging in any act contrary to these Terms. A repeat infringer is a user who has been notified of infringing activity more than once and/or has had a user submission removed from the App more than once. Further, at its sole discretion, <strong>AgroPlus</strong> reserves the right to decide whether any Content is appropriate and complies with these Terms.</li>\n" +
                "</ol>\n" +
                "&nbsp;\n" +
                "<h3 style=\"text-align: center;\"><u>Conduct</u></h3>\n" +
                "You agree not to post, email, host, display, upload, modify, publish, transmit, update or share any information on the App, or otherwise make available Content:\n" +
                "<ol>\n" +
                " \t<li>that violates any law or regulation;</li>\n" +
                " \t<li>that is harmful, abusive, unlawful, threatening, harassing, blasphemous, defamatory, obscene, pornographic, paedophilic, libellous, invasive of another's privacy or other rights, hateful, or racially, ethnically objectionable, disparaging,</li>\n" +
                "</ol>\n" +
                "<ul>\n" +
                " \t<li>that is copyrighted or patented, protected by trade secret or trademark, or otherwise subject to third party proprietary rights, including privacy and publicity rights, unless you are the owner of such rights or have permission or a license from their rightful owner to post the material and to grant <strong>AgroPlus</strong> all of the license rights granted herein;</li>\n" +
                "</ul>\n" +
                "<ol>\n" +
                " \t<li>that violates any local equal employment laws, including but not limited to those prohibiting the stating, in any advertisement for employment, a preference or requirement based on race, color, religion, sex, national origin, age, or disability of the applicant.</li>\n" +
                " \t<li>that harasses, degrades, intimidates or is hateful towards any individual or group of individuals on the basis of religion, gender, sexual orientation, race, ethnicity, age, or disability;</li>\n" +
                " \t<li>that includes personal or identifying information about another person without that person's explicit consent;</li>\n" +
                "</ol>\n" +
                "<ul>\n" +
                " \t<li>that impersonates any person or entity, including, but not limited to, an <strong>AgroPlus </strong>employee, or falsely states or otherwise misrepresents an affiliation with a person or entity;</li>\n" +
                " \t<li>deceives or misleads the addressee about the origin of such messages or communicates any information which is grossly offensive or menacing in nature;</li>\n" +
                "</ul>\n" +
                "<ol>\n" +
                " \t<li>that is false, deceptive, misleading, deceitful, misinformative, or constitutes \"<strong>bait and switch</strong>\" offer;</li>\n" +
                " \t<li>that constitutes or contains \"<strong>pyramid schemes</strong>\", “jokes”, \"<strong>affiliate marketing,</strong>\" \"<strong>link referral code,</strong>\" \"<strong>junk mail,</strong>\" \"<strong>spam,</strong>\" \"<strong>chain letters,</strong>\" or unsolicited advertisements of a commercial nature;</li>\n" +
                " \t<li>that constitutes or contains any form of advertising or solicitation if (1) posted in areas or categories of the Website which are not designated for such purposes; or (2) e-mailed to <strong>AgroPlus</strong> users who have requested not to be contacted about other services, products or commercial interests;</li>\n" +
                "</ol>\n" +
                "<ul>\n" +
                " \t<li>that includes links to commercial services or Third Party Websites, except as specifically allowed by <strong>AgroPlus</strong>;</li>\n" +
                " \t<li>that advertises any illegal services or the sale of any items the sale of which is prohibited or restricted by applicable law, including without limitation items the sale of which is prohibited or regulated by applicable law;</li>\n" +
                " \t<li>that contains software viruses or any other computer code, files or programs designed to interrupt, destroy or limit the functionality of any computer software or hardware or telecommunications equipment or any other computer resource;</li>\n" +
                "</ul>\n" +
                "<ol>\n" +
                " \t<li>that disrupts the normal flow of dialogue with an excessive number of messages to the Service, or that otherwise negatively affects other users' ability to use the Service; or</li>\n" +
                "</ol>\n" +
                "<ul>\n" +
                " \t<li>that employs misleading email addresses, or forged headers or otherwise manipulated identifiers in order to disguise the origin of Content transmitted through the Service.</li>\n" +
                "</ul>\n" +
                "&nbsp;\n" +
                "\n" +
                "Additionally, you agree not to:\n" +
                "<ol>\n" +
                " \t<li>contact anyone who has asked not to be contacted, or make unsolicited contact with anyone for any commercial purpose, specifically, contact any user to post advertisement on a third party Website/App or post any advertisement on behalf of such user; or to \"<strong>stalk</strong>\" or otherwise harass anyone;</li>\n" +
                " \t<li>make any libellous or defamatory comments or postings to or against anyone;</li>\n" +
                "</ol>\n" +
                "<ul>\n" +
                " \t<li>collect personal data about other users or entities for commercial or unlawful purposes;</li>\n" +
                "</ul>\n" +
                "<ol>\n" +
                " \t<li>use automated means, including spiders, robots, crawlers, data mining tools, or the like to download or scrape data from the Service, except for internet search engines (e.g,. Google) and non-commercial public archives (e.g. archive.org) that comply with our robots.txt file;</li>\n" +
                " \t<li>post Content that is outside the local area or not relevant to the local area, repeatedly post the same or similar Content, or otherwise impose unreasonable or disproportionately large loads on our servers and other infrastructure;</li>\n" +
                " \t<li>post the same item or service in multiple classified categories or forums, or in multiple metropolitan areas;</li>\n" +
                "</ol>\n" +
                "<ul>\n" +
                " \t<li>attempt to gain unauthorized access to computer systems owned or controlled by <strong>AgroPlus</strong> or engage in any activity that disrupts, diminishes the quality of, interferes with the performance of, or impairs the functionality of, the Service or the Website.</li>\n" +
                " \t<li>use any form of automated device or computer program that enables the use of <strong>AgroPlus</strong> \"<strong>flagging system</strong>\" or other community control systems without each flag being manually entered by a human that initiates the flag (an \"<strong>automated flagging device</strong>\"), or use any such flagging tool to remove posts of competitors, other third parties or to remove posts without a reasonable good faith belief that the post being flagged violates these Terms or any applicable law or regulation; or</li>\n" +
                "</ul>\n" +
                "<ol>\n" +
                " \t<li>use any automated device or software that enables the submission of automatic postings on <strong>AgroPlus</strong> without human intervention or authorship (an \"<strong>automated posting device</strong>\"), including without limitation, the use of any such automated posting device in connection with bulk postings, or for automatic submission of postings at certain times or intervals.</li>\n" +
                "</ol>\n" +
                "<strong>Any Content uploaded by you shall be subject to relevant laws and may disabled, or and may be subject to investigation under appropriate laws. Furthermore, if you are found to be in non-compliance with the laws and regulations, these terms, or the privacy policy of the Site, we may terminate your account/block your access to the Site and we reserve the right to remove any non-compliant Content uploaded by you.</strong>\n" +
                "\n" +
                "&nbsp;\n" +
                "\n" +
                "&nbsp;", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
