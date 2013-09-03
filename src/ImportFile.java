import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Arrays;


public class ImportFile
{
	private String fromPath;
	private String toPath;
	
	private File fromFileDir,
				 toFileDir;
	
	private String currentBook = null,
				   currentUnit = null,
				   currentSentence = null;
	
	
	public ImportFile(String from,String to)
	{
		fromPath = from;
		toPath = to;
		init();
	}
	
	private void init()
	{
		fromFileDir = new File(fromPath);
		assert(fromFileDir.exists()&&fromFileDir.isDirectory());
		
		toFileDir = new File(toPath);
		if(!toFileDir.exists())
			toFileDir.mkdirs();
		
	}
	
	public void onStart()
	{
		File[] allbookNameDirs = fromFileDir.listFiles();
		for(int i=0;i<allbookNameDirs.length;++i)
		{
			File toTxt = new File(toPath+"/"+allbookNameDirs[i].getName()+".gzc");
			
			currentBook = allbookNameDirs[i].getName();
			FileTool_jimuzaoju.appendText("\n正在读入"+currentBook);
			try
			{
				if(toTxt.exists())
				{
					toTxt.delete();
				}
				toTxt.createNewFile();
				RandomAccessFile writeTxt = new RandomAccessFile(toTxt, "rw");
				
				
				File[] allUnitNameTxt = allbookNameDirs[i].listFiles();
				Arrays.sort(allUnitNameTxt,new FileComparator());
				
				writeTxt.seek((long)12+allUnitNameTxt.length*4);
				for(int j=0;j<allUnitNameTxt.length;++j)
				{
					String name = allUnitNameTxt[j].getName();
					name = tidyUnitName(name);
					
					FileTool_jimuzaoju.appendText("正在读入"+currentBook + " " + name);
					
					writeTxt.write(name.getBytes("UTF-8"));
					if(j<allUnitNameTxt.length-1)
					{
					writeTxt.write(13);
					writeTxt.write(10);
					}
				}
				int unitNameLength = (int)writeTxt.getFilePointer() - (12+allUnitNameTxt.length*4);
				int[] unitPosition = new int[allUnitNameTxt.length]; 
				unitPosition[0]=(int)writeTxt.getFilePointer();
				writeTxt.seek(0);
				writeTxt.writeInt(0);
				writeTxt.writeInt(allUnitNameTxt.length);
				writeTxt.writeInt(unitNameLength);
				writeTxt.seek(unitPosition[0]);
				for(int j=0;j<allUnitNameTxt.length;++j)
				{
					unitPosition[j] = (int)writeTxt.getFilePointer();
					currentUnit = allUnitNameTxt[i].getName();
					InputStreamReader in = new InputStreamReader(new FileInputStream(allUnitNameTxt[j]),"UTF-8");
					BufferedReader bufferedReader = new BufferedReader(in);
					writeIntoFile(writeTxt,bufferedReader);
					bufferedReader.close();
				}
				writeTxt.seek(12);
				for(int j=0;j<unitPosition.length;++j)
				{
					writeTxt.writeInt(unitPosition[j]);
				}
				writeTxt.close();
				
			} catch (Exception e)
			{
			}
			FileTool_jimuzaoju.appendText("\n读入"+currentBook+"成功");
		}
		System.out.println("load file successful");
		FileTool_jimuzaoju.appendText("全部读入成功");
	}

	private void writeIntoFile(RandomAccessFile writeTxt,BufferedReader bufferedReader)
	{
		int state = 1;
		String s;
		boolean eof = false;
		try
		{
			while ((s = bufferedReader.readLine())!= null)
			{
				switch(state)
				{
				case 1:
					if(s.length() == 0)
					{
						eof = true;
						break;
					}
					writeTxt.write(s.getBytes("UTF-8"));
					currentSentence = s;
					break;
				case 2:
					if(s.length()==0)
					{
						System.out.println("中文解释缺失!");
						System.out.println(currentBook + " " +currentUnit + " "+ currentSentence);
						FileTool_jimuzaoju.appendText("中文解释缺失" + currentBook + " " +currentUnit + " "+ currentSentence);
						assert false;
					} 
					writeTxt.write(s.getBytes("UTF-8"));
					break;
				case 3:
					if(s.length()!=0)
					{
						System.out.println("Error when state 3");
						System.out.println(currentBook + " " +currentUnit + " "+ currentSentence);
						assert false;
					}
				}
				if(!eof&&state!=3)
				{
					writeTxt.write(13);
					writeTxt.write(10);
				}
			   if(eof)
				{
					break;
				}
				state = (state%3)+1;
			}
			if(state==2)
			{
				System.out.println("中文解释缺失!");
				System.out.println(currentBook + " " +currentUnit + " "+ currentSentence);
				FileTool_jimuzaoju.appendText("中文解释缺失" + currentBook + " " +currentUnit + " "+ currentSentence);
				assert false;				
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String tidyUnitName(String s)
	{
		String [] t = s.split("-");
		assert t.length == 2;
		t[1] = Tool.getIstance().removeSuffix(t[1]);
		return t[1];
	}
}
