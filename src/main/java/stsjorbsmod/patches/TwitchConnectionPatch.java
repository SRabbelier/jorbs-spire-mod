package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.gikk.twirk.Twirk;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import de.robojumper.ststwitch.TwitchPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TwitchConnectionPatch {

    public static final Logger logger = LogManager.getLogger(TwitchConnectionPatch.class.getName());

    public static String body = "";

    // Turn on logging of Twitch responses to figure out why it's broken.
    @SpirePatch(clz = Twirk.class, method = "doConnect")
    public static class Twirk_doConnect
    {
        @SpireInsertPatch(rloc = 11, localvars = {"line"})
        public static void patch(Twirk __this, String line)
        {
            if (line.contains(("GLHF"))) {
                logger.info("Successfully connected to twitch IRC.");
                body = "Connected to twitch IRC.\n";
            }
            if (line.contains(("Login authentication failed"))) {
                logger.info("Login authentication failed");
                body = "Login authentication failed\n";
            }
            logger.info("IN: " + line);
        }
    }

    // Turn on logging of Twitch responses to figure out why it's broken.
    @SpirePatch(clz = Twirk.class, method = "incommingMessage")
    public static class Twirk_incommingMessage
    {
        @SpirePrefixPatch
        public static void patch(Twirk __this, String line, String ___channel)
        {
            if (line.contains("twitch.tv JOIN #")) {
                logger.info("Successfully joined " + ___channel);
                body = "Joined " + ___channel + "\n";
            }
            logger.info("IN: " + line);
        }
    }

    // Turn on logging of Twitch responses to figure out why it's broken.
    @SpirePatch(clz = TwitchPanel.class, method = "updateInstructions")
    public static class TwitchPanel_updateInstructions
    {
        @SpirePostfixPatch()
        public static void patch(TwitchPanel __this, Hitbox ___hb)
        {
            if (___hb.hovered && !InputHelper.justClickedLeft) {
                float y = ___hb.y - 50.0f * Settings.scale;
                TipHelper.renderGenericTip(
                        ___hb.x, y,
                        "Twitch connection",
                        TwitchConnectionPatch.body);
            }
        }
    }


}