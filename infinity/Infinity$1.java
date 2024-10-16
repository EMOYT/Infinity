package infinity;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

class infinity$1 extends BaseDialog {
   private float countdown = 480.0F;
   private boolean exitable = false;

   infinity$1(String arg0) {
      super(arg0);
      this.update(() -> {
         this.countdown -= Time.delta;
         if (this.countdown < 0.0F && !this.exitable) {
            this.exitable = true;
            this.addCloseListener();
         }

      });
      this.cont.pane((t) -> {
         t.left();
         t.table().margin(60.0F).row();
         t.defaults().align(8).padLeft(12.0F).row();
         t.add("").update((b) -> {
            b.setText("[gray]Вы можете закрыть это через [accent]" + Mathf.ceil(Math.max(this.countdown, 0.0F) / 60.0F) + " [lightgray]sec.\n" + Core.bundle.format("startwarn.1", new Object[]{Mathf.ceil(Math.max(this.countdown, 0.0F) / 60.0F)}));
            if (this.countdown < 0.0F) {
               b.remove();
            }

         }).row();
         t.image().growX().height(4.0F).pad(12.0F).color(Pal.turretHeat).row();
         t.add("[gray]This Dialog Only Shows [lightgray]Once[] After Installation.").row();
         t.add(Core.bundle.get("startwarn.2")).row();
         t.image().growX().height(4.0F).pad(12.0F).color(Color.lightGray).row();
         t.add("[accent]Tips:").row();
         t.add("If the mod support your language (En, in_ID, ko, uk_UA, zh_CH) and the language of the mod doesn't fit yours, please set the language to another one, reload, set it to yours, reload again, and this should work.").padLeft(60.0F).row();
         t.add(Core.bundle.get("startwarn.3")).row();
         t.add(Core.bundle.get("startwarn.4")).padLeft(60.0F).row();
         t.image().growX().height(4.0F).pad(12.0F).color(Pal.heal).row();
         t.add("[lightgray]The warns below are special messages to Chinese players, so if you aren't Chinese, skip it.").row();
         t.add(Core.bundle.get("startwarn.5")).row();
         t.add(Core.bundle.get("startwarn.6")).padLeft(60.0F).row();
      }).grow().pad(90.0F).row();
      this.cont.button("", Styles.cleart, this::hide).update((b) -> {
         b.setDisabled(this.countdown > 0.0F);
         b.setText(this.countdown > 0.0F ? "[accent]" + Mathf.ceil(Math.max(this.countdown, 0.0F) / 60.0F) + " []sec." : Core.bundle.get("confirm"));
      }).growX().height(60.0F).pad(12.0F);
   }
}
