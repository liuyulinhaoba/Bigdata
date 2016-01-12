import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataPre {
  
  private static String key[]={"铜锅炭火锅","麻酱小料","餐具","麻汁小料","锅底","餐巾纸","碗","特色油碟"};
   private HashMap<String,String> productList=new HashMap<String,String>(); 
   private ArrayList<String>  orderList=new ArrayList<String>();
   private ArrayList<String> orderListCopy=new ArrayList<String>();
   private HashMap<String,Integer> productNum=new HashMap<String,Integer>();
   
   public static void  main(String agrs[]){
	   System.out.println("program running.......");
	   DataPre dataPre=new DataPre();
	   dataPre.readData("A.csv",6,1,13);
	   dataPre.readData("B.csv",6,1,13);
	   
	   dataPre.anaylizeData("A.csv",6,13);
	   dataPre.anaylizeData("B.csv",6,13);
	   
	   System.out.println("program down!!!");
   }
   
   
   public void readData(String path,int pIndex,int oIndex,int rIndex){
     try{
    	 File f = new File(path);  
    	 InputStreamReader fr = new InputStreamReader(new FileInputStream(f),  "GBK");  
	       BufferedReader br=new BufferedReader(fr);
	       String line=br.readLine();
	       String[] term=line.split(",");
	       
	       String hotal=term[rIndex];
	       String order=term[oIndex];
	       String temp="";
	       String path1="";
	       String path2="";
	       int max=0;
	       int flag=0;
	       
	       while(line!=null){
	    	    String[] tterm=line.split(",");
	    	    String thotal=tterm[rIndex];
	    	    String torder=tterm[oIndex];
	    	    String tproduct=tterm[pIndex];
	    	    flag=0;
	    	    if(!thotal.equals(hotal)){
	    	    	orderList.add(temp); 	
	    	     	path1=hotal+"菜品.txt";
	    	    	path2=hotal+"菜品编号.txt";
	    	    	writeData(path1,path2);
	    	    	cleanData();
	    	    	hotal=thotal;
	    	    	order=torder;
	    	    	temp="";
	    	    	max=0;
	    	    }
	    	    
	    	    if(!torder.equals(order)){
	    	         orderList.add(temp); 	
	    	         order=torder;
	                 temp="";
	    	    }
	    	    
	    	   for(int i=0;i<key.length;i++){
	    		     if(tproduct.equals(key[i])){
	    		    	 flag=1;
	    		    	 break;
	    		     }   
	    	   }
	    	   
	    	   if(flag==1){
	    		   line=br.readLine();
	    		   continue;
	    	   }
	    	   
	    	    if(productList.get(tproduct)==null){
	    	    	productList.put(tproduct,String.valueOf(max));
	    	    	max++;
	    	    }
	    	    
	    	    temp=temp+productList.get(tproduct)+",";
	    	    line=br.readLine();
	       }
	      br.close();
	      fr.close();
	      path1=hotal+"菜品.txt";
    	  path2=hotal+"菜品编号.txt";
	      writeData(path1,path2);
	      cleanData();
	   }
	   catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   public void writeData(String path1,String path2){
	   try{
	    FileWriter fw1=new FileWriter(path1);
	    BufferedWriter bw1=new BufferedWriter(fw1);
	    
	    for(int i=0;i<orderList.size();i++){
	    	String[] term=orderList.get(i).split(",");
	    	int size=term.length;
	    	int []temp=new int[size];
	    	for(int j=0;j<size;j++){
	    		if(term[j].length()!=0)
	    		temp[j]=Integer.parseInt(term[j]);
	    	}
	    	Arrays.sort(temp);
	    	String str="";
	    	int current=-1;
	    	for(int j=0;j<size;j++){
	    		if(current==temp[j]){
	    			continue;
	    		}
	    		else{
	    			current=temp[j];
	    		}
	    		if(j==size-1)
	    		  str=str+String.valueOf(temp[j]);
	    		else
	    		  str=str+String.valueOf(temp[j])+",";
	    	}
	    	 orderListCopy.add(str);
	    }
	   
	    for(int i=0;i<orderListCopy.size();i++){
	         bw1.write(orderListCopy.get(i));
	         bw1.write("\r\n");
	    }
	    bw1.close();
	    fw1.close();
	    
	    
	    FileWriter fw2=new FileWriter(path2);
	    BufferedWriter bw2=new BufferedWriter(fw2);
	    Iterator iter =productList.entrySet().iterator();
	    while (iter.hasNext()) {
	          Map.Entry  entry = (Map.Entry) iter.next();
	          String  key = (String)entry.getKey();
	          String  val =  (String)entry.getValue();
	          String line=key+","+val;
	          bw2.write(line);
	          bw2.write("\r\n");
	    }
        bw2.close();
        fw2.close();
	   }
	   catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   public void cleanData(){
	   productList.clear();
	   orderList.clear();
	   orderListCopy.clear();
   }
   
   public void anaylizeData(String path,int pIndex,int rIndex){
	try{
		File f = new File(path);  
   	    InputStreamReader fr = new InputStreamReader(new FileInputStream(f),  "GBK");  
	    BufferedReader br=new BufferedReader(fr);
	    String line=br.readLine();
	    String[] term=line.split(",");
	    String hotal=term[rIndex];
		String path1="";
		
		while(line!=null){
			 String[] tterm=line.split(",");
			 String thotal=tterm[rIndex];
	    	 String tproduct=tterm[pIndex];
	    	 if(!thotal.equals(hotal)){
	    		 path1=hotal+"菜品热度.txt";
	    		 hotal=thotal;
	    		 writeResult(path1);
	    		 cleanResult();
	    	 }
	    	 
	        if(productNum.get(tproduct)!=null){
	    			 int time=productNum.get(tproduct)+1;
	    			 productNum.remove(tproduct);
	    			 productNum.put(tproduct,time);
	    		 }
	        else
	    			productNum.put(tproduct,1);
	    	 line=br.readLine();
	}
		br.close();
		fr.close();
		path1=hotal+"菜品热度.txt";
		writeResult(path1);
		cleanResult();
	}
	catch(Exception e){
		e.printStackTrace();
	}   
   }
   
   
   public void writeResult(String path){
	   try{
	    FileWriter fw1=new FileWriter(path);
	    BufferedWriter bw1=new BufferedWriter(fw1);
	    List<Map.Entry<String, Integer>> infoIds =
	    	    new ArrayList<Map.Entry<String, Integer>>(productNum.entrySet());
	    Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
	        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {   
	        	if(o1.getValue()<o2.getValue())
	              return 1;
	        	else
				 return -1;
	        }
	    }); 
	
	  for (int i = 0; i < infoIds.size(); i++)  {
	          String line=infoIds.get(i).toString();
	          bw1.write(line);
	          bw1.write("\r\n");
	    }
	    bw1.close();
	    fw1.close();
	   }
	   catch(Exception e){
		   e.printStackTrace();
	   }
   }
  
   public void cleanResult(){
	   productNum.clear();
   }
   
}