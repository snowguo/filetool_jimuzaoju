import java.io.File;

public class Tool
{
	private static Tool instance;
	private Tool(){}
	
	public static Tool getIstance()
	{
		if (instance == null)
		{
			synchronized (Tool.class)
			{
				if (instance == null)
				{
					instance = new Tool();
				}
			}
		}
		return instance;
	}
	
	
	public File findFile(File[] filelist, String name)
	{
		for (int i = 0; i < filelist.length; ++i)
		{
			if (filelist[i].getName().equals(name))
				return filelist[i];
		}
		assert false;
		return null;
	}

	public int byteToint(byte[] b)
	{
		int a = 0;
		int t;
		for (int i = 0; i < 4; ++i)
		{
			t = (int) b[i];
			t = t & 0xff;
			a = (a << 8) + t;
		}
		return a;
	}

	public int[] byteArrayTointArray(byte[] b)
	{
		int[] a = new int[b.length / 4];
		byte[] t = new byte[4];
		for (int i = 0; i < a.length; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				t[j] = b[j + i * 4];
			}
			a[i] = byteToint(t);
		}
		return a;
	}
	
	public String removeSuffix(String fileName)
	{
		String s = "";
		int pos = -1;
		for(int i=fileName.length()-1;i>=0;--i)
		{
			if(fileName.charAt(i) == '.') 
			{
				pos = i-1;
				break;
			}
		}
		for(int i=pos;i>=0;--i)
		{
			s = fileName.charAt(i) + s;
		}
		return s;
	}
	
	
	
}
