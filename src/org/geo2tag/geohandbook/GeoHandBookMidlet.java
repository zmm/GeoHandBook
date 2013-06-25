package org.geo2tag.geohandbook;

import java.util.Stack;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.geo2tag.geohandbook.utils.GHBState;
import org.geo2tag.geohandbook.utils.StringConstants;

public class GeoHandBookMidlet extends MIDlet  {

	
	private LoginForm m_loginForm = new LoginForm();
	private ChannelsList m_channelsList = new ChannelsList();
	private TagsList m_tagsList = new TagsList();
	private Display m_display = Display.getDisplay(this);
	
	private TextForm m_aboutForm = new TextForm(StringConstants.ABOUT_HEADER, StringConstants.ABOUT_TEXT);
	private TextForm m_helpForm = new TextForm(StringConstants.HELP_HEADER, StringConstants.HELP_TEXT);
	
	private CommandListener m_commandListener = new CommandListener() {
		
		private Stack m_stack = new Stack();
		
		public void commandAction(Command arg0, Displayable arg1) {
			// TODO Auto-generated method stub
			System.out.println(arg0.toString()+" "+arg1.toString());
			
			if (arg0.getCommandType() == Command.OK && arg1 instanceof LoginForm){
				// Do login stuff and go to chat form in case of success
				System.out.println("OK action pressed!!!!");
				String token = m_loginForm.login();
				if (token != null){	
					GHBState.setAuthToken(token);
					m_loginForm.setDb();
					goForwardWithMemorizing(m_tagsList);
				}
			}else if (arg0.getCommandType() == Command.BACK || arg1 instanceof TextForm){
				goBackWithMemorizing();
					
			}else if (arg0.getLabel() == StringConstants.REFRESH_COMMAND ){
				
				m_tagsList.refreshData();
				
			}else if (arg0.getCommandType() == Command.OK && arg1 instanceof ChannelsList){
				
				m_channelsList.changeSubscription();
				goBackWithMemorizing();
			}else if (arg0.getLabel() == StringConstants.CHANNELS_COMMAND){
				m_channelsList.retrieveChannels();
				goForwardWithMemorizing(m_channelsList);
			}else if (arg0.getCommandType() == Command.EXIT){
				notifyDestroyed();
			}else if (arg0.getLabel() == StringConstants.ABOUT_COMMAND){
				goForwardWithMemorizing(m_aboutForm);
			}else if (arg0.getLabel() == StringConstants.HELP_COMMAND){
				goForwardWithMemorizing(m_helpForm);
			}
		}
				
		private void goBackWithMemorizing(){
			Displayable back = (Displayable) m_stack.pop();
			if (back instanceof TagsList)
				m_tagsList.startTagsListUpdate();
			
			if (m_display.getCurrent() instanceof TagsList)
				m_tagsList.stopTagsListUpdate();
				
			m_display.setCurrent(back);
			System.out.println(m_stack.toString());
		}
		
		private void goForwardWithMemorizing(Displayable newForm){
			Displayable current = m_display.getCurrent();
			m_stack.push(current);
			if ( current instanceof TagsList)
				m_tagsList.stopTagsListUpdate();
			m_display.setCurrent(newForm);
			if ( newForm instanceof TagsList)
				m_tagsList.startTagsListUpdate();
			System.out.println(m_stack.toString());
		}
	}; 
	
	
	
	
	
	public GeoHandBookMidlet() {
		// TODO Auto-generated constructor stub
		setupCommandListeners();
	}

	private void setupCommandListeners(){
		m_loginForm.setCommandListener(m_commandListener);
		m_tagsList.setCommandListener(m_commandListener);
		m_channelsList.setCommandListener(m_commandListener);
		m_helpForm.setCommandListener(m_commandListener);
		m_aboutForm.setCommandListener(m_commandListener);
	}
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
		m_display.setCurrent(m_loginForm);
	}


	


	public void showAlert(String text){
		Alert message = new Alert(null);
		message.setString(text);
		message.setTimeout(5000);
		Displayable currentDisplayable = m_display.getCurrent();
		m_display.setCurrent(message, currentDisplayable);
	}
}
