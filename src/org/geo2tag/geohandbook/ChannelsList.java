/**
 * 
 */
package org.geo2tag.geohandbook;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import org.geo2tag.geohandbook.utils.GHBState;
import org.geo2tag.geohandbook.utils.Settings;
import org.geo2tag.geohandbook.utils.StringConstants;

import com.nokia.mid.ui.IconCommand;

import ru.spb.osll.json.Errno;
import ru.spb.osll.json.JsonAvailableChannelsRequest;
import ru.spb.osll.json.JsonAvailableChannelsResponse;
import ru.spb.osll.json.JsonRequestException;
import ru.spb.osll.json.JsonSubscribeRequest;
import ru.spb.osll.json.JsonSubscribeResponse;
import ru.spb.osll.json.JsonSubscribedChannelsRequest;
import ru.spb.osll.json.JsonSubscribedChannelsResponse;
import ru.spb.osll.json.JsonUnsubscribeRequest;
import ru.spb.osll.json.JsonUnsubscribeResponse;
import ru.spb.osll.json.RequestSender;
import ru.spb.osll.objects.Channel;

/**
 * @author Mark Zaslavskiy
 *
 */
public class ChannelsList extends List {

	private Vector m_channels = new Vector();
	private Vector m_subscribedChannels = new Vector();
	
	private IconCommand m_cancelCommand = new IconCommand(StringConstants.BACK_COMMAND, Command.BACK, 0, IconCommand.ICON_BACK );
	private IconCommand m_okCommand = new IconCommand(StringConstants.OK_COMMAND, Command.OK, 1, IconCommand.ICON_OK);
	
	public ChannelsList(){
		super(StringConstants.CHANNELS, List.MULTIPLE);
		addCommand(m_cancelCommand);
		addCommand(m_okCommand);
	}
	
	public void retrieveChannels(){
		// Do available and subscribedChannels requests, in case of success refresh view
		m_channels = retrieveAvailableChannels();
		if (m_channels.size() == 0) return;
		m_subscribedChannels = retrieveSubscribedChannels();
		
		refreshView();
		
	}
	
	// Draw all channels and check subscribed
	private void refreshView(){
		deleteAll();
		System.out.println("SubscribedChannels "+m_subscribedChannels);
		for (int i=0; i<m_channels.size(); i++){
			Channel channel = (Channel)m_channels.elementAt(i);
			append(channel.getName(), null);
			setSelectedIndex(i, m_subscribedChannels.contains(channel));
			System.out.println("Channel = "+channel.getName()+" subscription = "+ m_subscribedChannels.contains(channel));
		}
	}
	


	public void changeSubscription() {
		// TODO Auto-generated method stub
		// Update subscribtion regardless to selection
		for (int i=0; i<m_channels.size(); i++){
			Channel channel = (Channel)m_channels.elementAt(i);
			
			boolean isSelected = isSelected(i);
			boolean isSubscribed = m_subscribedChannels.contains(channel);
			
			if (isSelected != isSubscribed){
				if (isSelected)
					subscribeChannel(channel.getName());
				else 
					unsubscribeChannel(channel.getName());
			}
		}
	}

	
	private void subscribeChannel(String channelName){
		JsonSubscribeRequest req = new JsonSubscribeRequest(GHBState.getAuthToken(), channelName, Settings.getServerUrl());
		JsonSubscribeResponse res = new JsonSubscribeResponse();
		
		int[] errnos = {Errno.SUCCESS.intValue(), Errno.CHANNEL_ALREADY_SUBSCRIBED_ERROR.intValue()};

		try {
			RequestSender.performRequest(req, res, errnos);
			System.out.println("SubscribeChannel was recieved succesfuly!");
						
		} catch (JsonRequestException e) {
			// TODO Auto-generated catch block
			
			System.out.println("SubscribeChannel failed!");
			e.printStackTrace();
		}		
	}
	
	private void unsubscribeChannel(String channelName){
		JsonUnsubscribeRequest req = new JsonUnsubscribeRequest(GHBState.getAuthToken(), channelName, Settings.getServerUrl());
		JsonUnsubscribeResponse res = new JsonUnsubscribeResponse();
		
		int[] errnos = {Errno.SUCCESS.intValue(), Errno.CHANNEL_NOT_SUBCRIBED_ERROR.intValue()};

		try {
			RequestSender.performRequest(req, res, errnos);
			System.out.println("UnsubscribeChannel was recieved succesfuly!");
						
		} catch (JsonRequestException e) {
			// TODO Auto-generated catch block
			
			System.out.println("UnsubscribeChannel failed!");
			e.printStackTrace();
		}
	}	
	
	private Vector retrieveAvailableChannels(){
		Vector result = new Vector();
		JsonAvailableChannelsRequest req = 
				new JsonAvailableChannelsRequest(GHBState.getAuthToken(), Settings.getServerUrl());
		JsonAvailableChannelsResponse res = new JsonAvailableChannelsResponse();
		
		int[] errnos = {Errno.SUCCESS.intValue()};
		
		try {
			RequestSender.performRequest(req, res, errnos);
			System.out.println("AvailableChannels was recieved succesfuly!");
			
			result = res.getChannels();
			
		} catch (JsonRequestException e) {
			// TODO Auto-generated catch block
			
			System.out.println("AvailableChannels failed!");
			e.printStackTrace();
		}
		return result;
	}
	
	private Vector retrieveSubscribedChannels(){
		Vector result = new Vector();
		JsonSubscribedChannelsRequest req = 
				new JsonSubscribedChannelsRequest(GHBState.getAuthToken(), Settings.getServerUrl());
		JsonSubscribedChannelsResponse res = new JsonSubscribedChannelsResponse();
		
		int[] errnos = {Errno.SUCCESS.intValue()};
		
		try {
			RequestSender.performRequest(req, res, errnos);
			System.out.println("SubscribedChannels was recieved succesfuly!");
			
			result = res.getChannels();
			
		} catch (JsonRequestException e) {
			// TODO Auto-generated catch block
			System.out.println("SubscribedChannels failed!");
			e.printStackTrace();
		}
		return result;
	}
}
