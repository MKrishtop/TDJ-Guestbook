package mkrishtop.servlet;

import java.util.HashMap;
import java.util.Map;

import mkrishtop.servlet.actions.AddAnswerAction;
import mkrishtop.servlet.actions.AddBookAction;
import mkrishtop.servlet.actions.AddCommentAction;
import mkrishtop.servlet.actions.AddModerAction;
import mkrishtop.servlet.actions.ChangeBookAction;
import mkrishtop.servlet.actions.ChangeCommentAction;
import mkrishtop.servlet.actions.DeleteBookAction;
import mkrishtop.servlet.actions.DeleteCommentAction;
import mkrishtop.servlet.actions.LoginAction;
import mkrishtop.servlet.actions.LogoutAction;
import mkrishtop.servlet.actions.RegisterAction;
import mkrishtop.servlet.actions.RemoveModerAction;

public class ActionFactory {
    protected Map map = defaultMap();
    
    public ActionFactory() {
         super();
    }
    public Action create(String actionName) {
         Class klass = (Class) map.get(actionName);
         if (klass == null)
              throw new RuntimeException(getClass() + " was unable to find " +
              		"an action named '" + actionName + "'.");
         
         Action actionInstance = null;
         try {
              actionInstance = (Action) klass.newInstance();
         } catch (Exception e) {
              e.printStackTrace();
         }

         return actionInstance;
    }
    protected Map defaultMap() {
         Map map = new HashMap();

         map.put("addAnswerAction", AddAnswerAction.class);
         map.put("addBookAction", AddBookAction.class);
         map.put("addCommentAction", AddCommentAction.class);
         
         map.put("addModerAction", AddModerAction.class);
         map.put("changeBookAction", ChangeBookAction.class);
         map.put("changeCommentAction", ChangeCommentAction.class);
         
         map.put("deleteBookAction", DeleteBookAction.class);
         map.put("deleteCommentAction", DeleteCommentAction.class);
         map.put("loginAction", LoginAction.class);
         
         map.put("logoutAction", LogoutAction.class);
         map.put("registerAction", RegisterAction.class);
         map.put("removeModerAction", RemoveModerAction.class);

         return map;
    }

}
