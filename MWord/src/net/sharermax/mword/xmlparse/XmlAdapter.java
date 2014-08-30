package net.sharermax.mword.xmlparse;
/********************
 * XML Adapter 
 * author: SharerMax
 * create: 2014.06.08
 * modify: 2014.06.22
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import net.sharermax.mword.database.DBAdapter;
import net.sharermax.mword.database.Word;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;


public class XmlAdapter {
	
	private FileOutputStream fileOutputStream;
	private XmlSerializer xmlSerializer;
	
	public XmlAdapter() {
		// TODO Auto-generated constructor stub
	}
	
	public int xmlExport(DBAdapter db, String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			//若父路径不存在 连同父路径新建
			folder.mkdirs();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());
		String filename = dateFormat.format(date) + ".xml";
		File xmlBackup = new File(folder, filename);
		try {
			fileOutputStream = new FileOutputStream(xmlBackup);
			xmlSerializer = Xml.newSerializer();
			try {
				xmlSerializer.setOutput(fileOutputStream, "UTF-8");
				xmlSerializer.startDocument("UTF-8", true);
				xmlSerializer.startTag(null, "words");
				Word []words = db.queryAllData();
				for(Word word : words) {
					xmlSerializer.startTag(null, "item");
					xmlSerializer.attribute(null, word.spelling, word.explanation);
					xmlSerializer.endTag(null, "item");
				}
				xmlSerializer.endTag(null, "words");
				xmlSerializer.endDocument();
				fileOutputStream.flush();
				fileOutputStream.close();
				return words.length;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public int xmlImport(DBAdapter db, String path) {
		XmlPullParser xmlPullParser = Xml.newPullParser();
		File importfile = new File(path);
		if (!importfile.exists()) {
			return -1;
		} else {
			try {
				FileInputStream fileInputStream = new FileInputStream(importfile);
				try {
					xmlPullParser.setInput(fileInputStream, "UTF-8");
					try {
						
						int count = 0;
						while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
							String itemname = xmlPullParser.getName();
							
							if ( (itemname!=null) && (xmlPullParser.getEventType() == XmlPullParser.START_TAG) ) {
								if (itemname.equals("item")) {
									String attrname = xmlPullParser.getAttributeName(0);
									String attrvalue = xmlPullParser.getAttributeValue(0);
									if (attrname != null) {
										Word word = new Word();
										word.spelling = attrname;
										word.explanation = attrvalue;
										db.insert(word);
										count++;
									}
								}

							}
							
						}
						return count;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return 0;
	}
	
}
