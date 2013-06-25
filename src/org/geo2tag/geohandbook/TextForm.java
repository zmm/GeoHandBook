/**
 * 
 */
package org.geo2tag.geohandbook;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

import org.geo2tag.geohandbook.utils.StringConstants;

import com.nokia.mid.ui.IconCommand;

/**
 * @author user
 *
 */
public class TextForm extends TextBox{

//	private StringItem m_text = new StringItem("", "");
		
	private IconCommand m_backCommand = new IconCommand(StringConstants.BACK_COMMAND, Command.BACK, 0, IconCommand.ICON_BACK);
	private IconCommand m_okCommand = new IconCommand(StringConstants.OK_COMMAND, Command.OK, 1, IconCommand.ICON_OK);

	
	public TextForm(String header, String text){
		super(header, text, text.length(), 0);
		setConstraints(TextField.UNEDITABLE);
		//m_text.setLabel(text);
		addCommand(m_backCommand);
		addCommand(m_okCommand);
		//append(m_text);
	}
	
	
	


}
