package com.nitara.CattleRegistration;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nitara.FarmManagement.ViewFarmCattleList;
import com.nitara.base.GenericBase;

public class GetCattleId extends GenericBase{
	
	public String getCattleId(String tagnumber, String farmId) {
		
		ViewFarmCattleList cattle = new ViewFarmCattleList();
		String response = cattle.viewFarmCattleList(farmId);

		JSONObject cattleList = new JSONObject(response);
		JSONArray farmCattle = cattleList.getJSONArray("farmCattle");

		String cattleId = "";
		
		System.out.println(farmCattle.length());
		for(int i =0 ;i<farmCattle.length();i++) {
			JSONObject details = farmCattle.getJSONObject(i);
			if(tagnumber.equals(details.getString("tagNumber"))){
				cattleId = details.getString("cattleId");
			}

		}
		
		return cattleId;
		
	}

}
