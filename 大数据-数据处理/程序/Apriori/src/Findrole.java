import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Findrole {
	private double minconf=0.3;
	public void isrole(String[] temp1,LinkedList<String> list,FileWriter tgFileWriter,String[] tempresult) throws IOException
	{
		String x="",y="";
		int total=Integer.parseInt(temp1[temp1.length-1]);
		int divi = 0;
		for (int i = 1; i < (1 << (temp1.length-1)) - 1; i++) {
			   x="";
			   y="";
			   for (int j = 0; j < temp1.length-1; j++) {
			    if (((1 << j) & i) != 0) {
			    	x+=temp1[j]+" ";
			    	
			    }
			   }

			   for (int j = 0; j < temp1.length-1; j++) {
			    if (((1 << j) & (~i)) != 0) {
			    	y+=temp1[j]+" ";

			    }
			   }
			   for(int z=0;z<list.size();z++)
			   {
				   String[] ttt = list.get(z).split(" ");
				   String[] tt1 = x.split(" " );
				   boolean flag = false;
				   if(ttt.length-1==tt1.length)
				   {
					   int length = 0;
					   for(int k=0;k<tt1.length;k++)
					   {
						   for(int p=0;p<ttt.length-1;p++)
						   {
							   if(tt1[k].equals(ttt[p]))
							   {
								   
								   length++;
								   break;
							   }
						   }
					   }
					   if(length==tt1.length)
					   {
						   flag=true;
					   }
				   }
				   if(flag)
				   {
					   String[] nm=list.get(z).split(" ");
					   divi=Integer.parseInt(nm[nm.length-1]);
					   double conf=(double)((double)total/(double)divi);
					   if(conf>=minconf)
					   {
						   String[] tmpx = x.split(" ");
						   for(int k=0;k<tmpx.length;k++)
						   {
							   tgFileWriter.append(tempresult[Integer.parseInt(tmpx[k])]+" ");
						   }
						   tgFileWriter.append("---->");
						   String[] tmpy = y.split(" ");
						   for(int k=0;k<tmpy.length;k++)
						   {
							   tgFileWriter.append(tempresult[Integer.parseInt(tmpy[k])]+" ");
						   }
						   tgFileWriter.append("    "+conf+"\n");
						   
					   }
					   break;
				   }
			   }
		}
	}
	public void result(String src) throws IOException
	{
		String srcresult = "Apriori关联规则/"+src+"结果.txt";
		String srcfile1="Apriori关联规则/"+src+"频繁集.txt";
		String srcfile2="数据/"+src+"菜品编号.txt";
		LinkedList<String> list = new LinkedList<String>();
		FileWriter tgFileWriter = new FileWriter(srcresult);
		String tmp = new String();
		String tmp1= new String();
		String[] tempresult = new String[1000];
		Findrole a = new Findrole();
		try{
			File file1 = new File(srcfile1);
			FileReader fr1 = new FileReader(file1);
			BufferedReader br1 = new BufferedReader(fr1);
			File file2 = new File(srcfile2);
			FileReader fr2 = new FileReader(file2);
			BufferedReader br2 = new BufferedReader(fr2);
			while(null!=(tmp=br1.readLine()))
			{
			    list.add(tmp);
			}
			while(null!=(tmp1=br2.readLine()))
			{
			    String[] ter = tmp1.split(",");
			    tempresult[Integer.parseInt(ter[1])]=ter[0];
			}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		for(int i=0;i<list.size();i++)
		{
			String temp = list.get(i);
			String[] temp1=temp.split(" ");
			if(temp1.length>2)
			{
				a.isrole(temp1,list,tgFileWriter,tempresult);
			}
			
			
		}
		tgFileWriter.close();
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String src = "";
		Findrole a = new Findrole();
		try{
			File file1 = new File("数据/list.txt");
			FileReader fr1 = new FileReader(file1);
			BufferedReader br1 = new BufferedReader(fr1);
			while(null!=(src=br1.readLine()))
			{
				String[] src1=src.split(",");
			    a.result(src1[0]);
			}
		
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
	
	}

}
