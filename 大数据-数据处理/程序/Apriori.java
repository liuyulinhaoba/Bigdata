import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;



public class Apriori {
	private int minSup;//最小支持度
	private static List<Set<String>> dataTrans;
	
	public int getMinSup() {
		return minSup;
	}
	
	public void setMinSup(int minSup) {
		this.minSup = minSup;
	}
    public void result(String srcFile,String targetFile,double minsup) throws IOException
    {
    	Apriori apriori = new Apriori();
		double [] threshold = {0.05};
		dataTrans = apriori.readTrans(srcFile);
		for(int k = 0; k < threshold.length; k++){
			System.out.println(srcFile + " threshold: " + threshold[k]);
			long totalItem = 0;
			long totalTime = 0;
			FileWriter tgFileWriter = new FileWriter(targetFile);
			apriori.setMinSup((int)(dataTrans.size() * minsup));
			long startTime = System.currentTimeMillis();
			Map<String, Integer> f1Set = apriori.findFP1Items(dataTrans);
			long endTime = System.currentTimeMillis();
			totalTime += endTime - startTime;
			Map<Set<String>, Integer> f1Map = new HashMap<Set<String>, Integer>();
			for(Map.Entry<String, Integer> f1Item : f1Set.entrySet()){
				Set<String> fs = new HashSet<String>();
				fs.add(f1Item.getKey());
				f1Map.put(fs, f1Item.getValue());
			}
			
			totalItem += apriori.printMap(f1Map, tgFileWriter);
			Map<Set<String>, Integer> result = f1Map;
			do {	
				startTime = System.currentTimeMillis();
				result = apriori.genNextKItem(result);
				endTime = System.currentTimeMillis();
				totalTime += endTime - startTime;
				totalItem += apriori.printMap(result, tgFileWriter);
			} while(result.size() != 0);
			tgFileWriter.close();
			System.out.println("共用时：" + totalTime + "ms");
			System.out.println("共有" + totalItem + "项频繁模式");
		}
    }

	 public static void main(String[] args) throws IOException { 
		 Apriori apriori = new Apriori();
		 String tmpsrc;
		 String srcFile = "";
	     String targetFile = "";
		 try{
				File file1 = new File("数据/list.txt");
				FileReader fr1 = new FileReader(file1);
				BufferedReader br1 = new BufferedReader(fr1);
				while(null!=(tmpsrc=br1.readLine()))
				{
					String[] tmpsrc1 = tmpsrc.split(",");
				    srcFile="数据/"+tmpsrc1[0]+"菜品.txt";
				    targetFile="Apriori关联规则/"+tmpsrc1[0]+"频繁集.txt";
				    apriori.result(srcFile, targetFile,Double.parseDouble(tmpsrc1[1]));
				}
				
				}catch (FileNotFoundException e) {
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}
	
	     
	}

	private Map<Set<String>, Integer> genNextKItem(Map<Set<String>, Integer> preMap) {
		// TODO Auto-generated method stub
		Map<Set<String>, Integer> result = new HashMap<Set<String>, Integer>();
		List<Set<String>> preSetArray = new ArrayList<Set<String>>();
		for(Map.Entry<Set<String>, Integer> preMapItem : preMap.entrySet()){
			preSetArray.add(preMapItem.getKey());
		}
		int preSetLength = preSetArray.size();
		for (int i = 0; i < preSetLength - 1; i++) {
			for (int j = i + 1; j < preSetLength; j++) {
				String[] strA1 = preSetArray.get(i).toArray(new String[0]);
				String[] strA2 = preSetArray.get(j).toArray(new String[0]);
				if (isCanLink(strA1, strA2)) { 
					Set<String> set = new TreeSet<String>();
					for (String str : strA1) {
						set.add(str);
					}
					set.add((String) strA2[strA2.length - 1]); 
					if (!isNeedCut(preMap, set)) {
						result.put(set, 0);
					}
				}
			}
		}
		return assertFP(result);
	}
	

	private boolean isNeedCut(Map<Set<String>, Integer> preMap, Set<String> set) {
		// TODO Auto-generated method stub
		boolean flag = false;
		List<Set<String>> subSets = getSubSets(set);
		for(Set<String> subSet : subSets){
			if(!preMap.containsKey(subSet)){
				flag = true;
				break;
			}
		}
		return flag;
	}

	private List<Set<String>> getSubSets(Set<String> set) {
		// TODO Auto-generated method stub
		String[] setArray = set.toArray(new String[0]);
		List<Set<String>> result = new ArrayList<Set<String>>();
		for(int i = 0; i < setArray.length; i++){
			Set<String> subSet = new HashSet<String>();
			for(int j = 0; j < setArray.length; j++){
				if(j != i) subSet.add(setArray[j]);
			}
			result.add(subSet);
		}
		return result;
	}


	private Map<Set<String>, Integer> assertFP(
			Map<Set<String>, Integer> allKItem) {
		// TODO Auto-generated method stub
		Map<Set<String>, Integer> result = new HashMap<Set<String>, Integer>();
		for(Set<String> kItem : allKItem.keySet()){
			for(Set<String> data : dataTrans){
				boolean flag = true;
				for(String str : kItem){
					if(!data.contains(str)){
						flag = false;
						break;
					}
				}
				if(flag) allKItem.put(kItem, allKItem.get(kItem) + 1);
			}
			if(allKItem.get(kItem) >= minSup) {
				result.put(kItem, allKItem.get(kItem));
			}
		}
		return result;
	}


	private boolean isCanLink(String[] strA1, String[] strA2) {
		// TODO Auto-generated method stub
		boolean flag = true;
		if(strA1.length != strA2.length){
			return false;
		}else {
			for(int i = 0; i < strA1.length - 1; i++){
				if(!strA1[i].equals(strA2[i])){
					flag = false;
					break;
				}
			}
			if(strA1[strA1.length -1].equals(strA2[strA1.length -1])){
				flag = false;
			}
		}
		return flag;
	}

	private int printMap(Map<Set<String>, Integer> f1Map, FileWriter tgFileWriter) throws IOException {
		// TODO Auto-generated method stub
		for(Map.Entry<Set<String>, Integer> f1MapItem : f1Map.entrySet()){
			for(String p : f1MapItem.getKey()){
				tgFileWriter.append(p + " ");
			}
			tgFileWriter.append( f1MapItem.getValue() + "\n");
		}
		tgFileWriter.flush();
		return f1Map.size();
	}
	

	private Map<String, Integer> findFP1Items(List<Set<String>> dataTrans) {
		// TODO Auto-generated method stub
		Map<String, Integer> result = new HashMap<String, Integer>();
		Map<String, Integer> itemCount = new HashMap<String, Integer>();
		for(Set<String> ds : dataTrans){
			for(String d : ds){
				if(itemCount.containsKey(d)){
					itemCount.put(d, itemCount.get(d) + 1);
				} else {
					itemCount.put(d, 1);
				}
			}
		}
		
		for(Map.Entry<String, Integer> ic : itemCount.entrySet()){
			if(ic.getValue() >= minSup){
				result.put(ic.getKey(), ic.getValue());
			}
		}
		return result;
	}


	private List<Set<String>> readTrans(String fileDir) {
		// TODO Auto-generated method stub
		List<Set<String>> records = new ArrayList<Set<String>>(); 
        try { 
            FileReader fr = new FileReader(new File(fileDir)); 
            BufferedReader br = new BufferedReader(fr); 
       
            String line = null; 
            while ((line = br.readLine()) != null) { 
                if (line.trim() != "") { 
                    Set<String> record = new HashSet<String>(); 
                    String[] items = line.split(","); 
                    for (String item : items) { 
                        record.add(item); 
                    } 
                    records.add(record); 
                } 
            } 
        } catch (IOException e) { 
            System.out.println("读取事务文件失败。"); 
            System.exit(-2); 
        } 
        return records; 
	}
}
 