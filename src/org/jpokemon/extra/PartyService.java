package org.jpokemon.extra;

import org.jpokemon.pokemon.ConditionEffect;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.server.JPokemonService;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PartyService implements JPokemonService{

	@Override
	public void login(Player player) {
	}

	@Override
	public void logout(Player player) {
	}

	@Override
	public void serve(JSONObject request, Player player) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject load(JSONObject request, Player player) {
		
		JSONObject json = new JSONObject();
		JSONArray pokemonArray = new JSONArray();
		
		try {
			json.put("action", "party");
			json.put("pokemon", pokemonArray);
			json.put("count", player.party().size());
			json.put("awakecount", player.party().awake());
			
			for (Pokemon pokemon : player.party()){
				JSONObject pokemonJson = new JSONObject();
				JSONArray conditionArray = new JSONArray();
				
				pokemonJson.put("name", pokemon.name());
				pokemonJson.put("number", pokemon.number());
				pokemonJson.put("health", pokemon.health());
				pokemonJson.put("maxhealth", pokemon.maxHealth());
				pokemonJson.put("condition", conditionArray);
				pokemonJson.put("level", pokemon.level());
				
				for(ConditionEffect C : pokemon.getConditionEffects()) {
					conditionArray.put(C.name());
				}
				
				pokemonArray.put(pokemonJson);
			}
		} catch (JSONException e) {
		}
		
		return json;
	}
	
}
