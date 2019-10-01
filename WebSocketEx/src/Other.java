
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Other {
	public Other() {
		System.out.println("Ohter Data.....");
	}
	
	public JsonElement getOtherData() {

		JsonObject jo = new JsonObject();
		
		jo.addProperty("name", "cobus");
		jo.addProperty("age", 999);
		
		JsonArray phones = new JsonArray();
		phones.add("010-1234-5678");
		phones.add("031-123-4567");
		jo.add("phones", phones);
		
		JsonArray children = new JsonArray();
		
		JsonObject child1 = new JsonObject();
		child1.addProperty("name", "mozzi");
		child1.addProperty("age", "5");
		children.add(child1);
		
		JsonObject child2 = new JsonObject();
		child2.addProperty("name", "mozza");
		child2.addProperty("age", "3");
		children.add(child2);
		
		jo.add("children", children);
		
		return jo;
		
		//Gson gson = new Gson();
		//return gson.toJson(jo);
		
		
		/*
		List<String> otherData = new ArrayList<String>();
		
		String name1 = "aaa";
		String name2 = "bbb";
		String name3 = "ccc";
		
		otherData.add(name1);
		otherData.add(name2);
		otherData.add(name3);
		
		Gson gson = new Gson();
		return gson.toJson(otherData);
		*/
	
	}
}
