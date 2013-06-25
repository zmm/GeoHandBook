/**
 * 
 */
package org.geo2tag.geohandbook;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;
import javax.microedition.location.QualifiedCoordinates;

import org.geo2tag.geohandbook.utils.DateUtil;
import org.geo2tag.geohandbook.utils.GHBState;
import org.geo2tag.geohandbook.utils.LocationUtil;
import org.geo2tag.geohandbook.utils.Settings;
import org.geo2tag.geohandbook.utils.StringConstants;

import ru.spb.osll.json.Errno;
import ru.spb.osll.json.JsonFilterCircleRequest;
import ru.spb.osll.json.JsonFilterResponse;
import ru.spb.osll.json.JsonRequestException;
import ru.spb.osll.json.RequestSender;
import ru.spb.osll.objects.Channel;
import ru.spb.osll.objects.Mark;

import com.nokia.mid.ui.IconCommand;

/**
 * @author Mark Zaslavskiy
 *
 */
public class TagsList extends List {

	


	
	private Timer m_timer = new Timer();
	
	private class CustomTimerTask extends TimerTask{
			    public void run() {
	    		refreshData();
	    }
	}
	
	private TimerTask m_timerTask = new CustomTimerTask(); 
	
	
	private IconCommand m_backCommand = new IconCommand(StringConstants.BACK_COMMAND, Command.BACK, 0, IconCommand.ICON_BACK);
	private Command m_refreshCommand = new Command(StringConstants.REFRESH_COMMAND, Command.HELP, 2);
	private Command m_channelsCommand = new Command(StringConstants.CHANNELS_COMMAND, Command.HELP, 3);
	private Command m_aboutCommand = new Command(StringConstants.ABOUT_COMMAND, Command.HELP, 4);
	private Command m_helpCommand = new Command(StringConstants.HELP_COMMAND, Command.HELP, 5);
	private Command m_exitCommand = new Command(StringConstants.EXIT_COMMAND, Command.EXIT, 6);

	
	public TagsList(){
		this("",List.IMPLICIT);
		setFitPolicy(TEXT_WRAP_ON);
		
		addCommand(m_backCommand);
		addCommand(m_refreshCommand);
		addCommand(m_channelsCommand);
		addCommand(m_aboutCommand);
		addCommand(m_helpCommand);
		addCommand(m_exitCommand);
	}
	
	public TagsList(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public void startTagsListUpdate(){
		if (Settings.isPeriodicalUpdateEnabled())
			m_timer.schedule(m_timerTask, 0, Settings.getUpdateInterval());
		else 
			refreshData();
	}
	
	public void stopTagsListUpdate(){
		if (Settings.isPeriodicalUpdateEnabled()){
			m_timer.cancel();
			m_timer = new Timer();
			m_timerTask = new CustomTimerTask();
		}
	}

	public void refreshData(){
		System.out.println("Going to do FilterCircle");
    	QualifiedCoordinates q = LocationUtil.getCoordinates();
    	
    	if (q == null) return;
    	
    	double latitude = q.getLatitude();
    	double longitude = q.getLongitude();
    	
    	String timeTo = DateUtil.getCurrentTime();
    	String timeFrom = DateUtil.getPastTime();
    	
    	JsonFilterCircleRequest req = new JsonFilterCircleRequest(GHBState.getAuthToken(),
    			latitude, longitude, Settings.getRadius(), timeFrom, timeTo,
    			Settings.getServerUrl()); 

        JsonFilterResponse res = new JsonFilterResponse();
        
        int[] errnos = {Errno.SUCCESS.intValue()};
		
		try {
			RequestSender.performRequest(req, res, errnos);
			System.out.println("FilterCircle was done succesfuly!");
			Vector channels = res.getChannelsData();
			setChannels(channels);
			
		} catch (JsonRequestException e) {
			// TODO Auto-generated catch block
			
			System.out.println("FilterCircle failed!");
			e.printStackTrace();
		}
	}

	private void setChannels(Vector channels) {
		// TODO Auto-generated method stub
		// Display tags regardless to channels
		deleteAll();
		for (int i=0; i<channels.size(); i++){
			Channel channel = (Channel) channels.elementAt(i);
			Vector marks = channel.getMarks();
			append("--"+channel.getName(), null);
			for (int j=0; j<marks.size(); j++){
				Mark mark = (Mark) marks.elementAt(j); 
				append(mark.getTitle() + " : " + mark.getDescription(), null);
			}
			
		}
	}

}
