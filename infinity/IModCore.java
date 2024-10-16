package infinity;

import arc.ApplicationListener;
import arc.Core;
import arc.util.Log;
import mindustry.Vars;
import infinity.expand.IVars;
import infinity.expand.cutscene.ICSS_Core;

public class IModCore implements ApplicationListener {
   public static IModCore core;
   public static IInputControl control;
   public IInputListener inputListener;
   public IRenderer renderer;

   public IModCore() {
      if (infinity.DEBUGGING) {
         Log.info("");
      }

      if (!Core.app.isHeadless()) {
         IVars.listener = this.inputListener = new IInputListener();
         IVars.renderer = this.renderer = new IRenderer();
         control = new IInputControl();
      }

      core = this;
      IVars.core = this;
   }

   public void update() {
      if (Vars.state.isPlaying()) {
         ICSS_Core.core.update();
         IGroups.update();
         if (!Vars.headless) {
            if (this.inputListener != null) {
               this.inputListener.update();
            }

            this.renderer.effectDrawer.update();
            this.renderer.textureStretchIn.update();
            ISetting.update();
         }
      }

   }

   public void dispose() {
      if (infinity.DEBUGGING) {
         Log.info("");
      }

   }

   public void init() {
      if (infinity.DEBUGGING) {
         Log.info("");
      }

   }

   public void initOnLoadWorld() {
      if (!Vars.headless) {
         this.renderer.init();
      }

   }
}
