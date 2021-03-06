package edu.illinois.finalproject;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiAsync;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionList;
import net.rithms.riot.api.endpoints.static_data.dto.Item;
import net.rithms.riot.api.endpoints.static_data.dto.ItemList;
import net.rithms.riot.api.endpoints.static_data.dto.SummonerSpell;
import net.rithms.riot.api.endpoints.static_data.dto.SummonerSpellList;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.api.request.AsyncRequest;
import net.rithms.riot.api.request.RequestAdapter;
import net.rithms.riot.constant.Platform;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

        @Test
        public void LolApiProfileRequest() throws Exception {
            ApiConfig config = new ApiConfig().setKey(LoLApiKey.apiKey);
            RiotApi api = new RiotApi(config);

            Summoner summoner = api.getSummonerByName(Platform.NA, "Leaf sin");
            System.out.println("Name: " + summoner.getName());
            System.out.println("Summoner ID: " + summoner.getId());
            System.out.println("Account ID: " + summoner.getAccountId());
            System.out.println("Summoner Level: " + summoner.getSummonerLevel());
            System.out.println("Profile Icon ID: " + summoner.getProfileIconId());
        }

    @Test
    public void LolApiProfileAsyncRequest() throws Exception {
        ApiConfig config = new ApiConfig().setKey(LoLApiKey.apiKey);
        RiotApi api = new RiotApi(config);
        RiotApiAsync apiAsync = api.getAsyncApi();

        long summonerId = 20987694; // summonerId to request
        Platform platform = Platform.EUW; // platform to request
        final ExtendedSummoner eSummoner = new ExtendedSummoner(); // Object where we want to store the data

        // Asynchronously get summoner information
        AsyncRequest requestSummoner = apiAsync.getSummoner(platform, summonerId);
        requestSummoner.addListeners(new RequestAdapter() {
            @Override
            public void onRequestSucceeded(AsyncRequest request) {
                eSummoner.summoner = request.getDto();
            }
        });
        try {
            // Wait for all asynchronous requests to finish
            apiAsync.awaitAll();
        } catch (InterruptedException e) {
            // We can use the Api's logger
            RiotApi.log.log(Level.SEVERE, "Waiting Interrupted", e);
        }
        System.out.println(eSummoner.summoner.getName());
    }

    @Test
    public void LolChampionMap() throws Exception {
        ApiConfig config = new ApiConfig().setKey(LoLApiKey.apiKey);
        RiotApi api = new RiotApi(config);
        RiotApiAsync apiAsync = api.getAsyncApi();
        final ExtendedSummoner extendedSummoner = new ExtendedSummoner();

        // get champion information
        AsyncRequest requestChampion = apiAsync.getDataChampionList(Platform.NA);
        requestChampion.addListeners(new RequestAdapter() {
            @Override
            public void onRequestSucceeded(AsyncRequest request) {
                LolConstants.champions = request.getDto();
            }
        });
        requestChampion.await();

        if (requestChampion.isDone()){
        // creating champion map by id
            for (net.rithms.riot.api.endpoints.static_data.dto.Champion c : LolConstants.champions.getData().values()) {
                LolConstants.championMap.put(c.getId(), c);
            }
        }
    }

    @Test
    public void LolChampionMap2() throws Exception {
        ApiConfig config = new ApiConfig().setKey(LoLApiKey.apiKey);
        RiotApi api = new RiotApi(config);
        final ExtendedSummoner extendedSummoner = new ExtendedSummoner();

        LolConstants.champions = api.getDataChampionList(Platform.NA);
        for (net.rithms.riot.api.endpoints.static_data.dto.Champion c : LolConstants.champions.getData().values()) {
            LolConstants.championMap.put(c.getId(), c);
            System.out.println(c.getName());
        }
    }

        @Test
        public void constantData() throws Exception {
            ApiConfig config = new ApiConfig().setKey(LoLApiKey.apiKey);
            RiotApi api = new RiotApi(config);


            ItemList itemList;
            Map<String,Item> itemMap = new HashMap<>();

            itemList = api.getDataItemList(Platform.JP);
            System.out.println(itemList.getData().values());

            for (Item i : itemList.getData().values()) {
                itemMap.put(i.getName(), i);
            }
            System.out.println(itemMap.get("Statikk Shiv").getId());
//                for (Item a : itemList.getData().values()){
//                    System.out.println(a.getName());
//                }


//            SummonerSpellList spellList;
//            spellList = api.getDataSummonerSpellList(Platform.OCE);
//            for (SummonerSpell a : spellList.getData().values()){
//                System.out.println(a.getName());
//            }
        }

    @Test
    public void rndmData() throws Exception {
        ApiConfig config = new ApiConfig().setKey(LoLApiKey.apiKey);
        RiotApi api = new RiotApi(config);
        RiotApiAsync apiAsync = api.getAsyncApi();
        final ExtendedSummoner eSummoner = new ExtendedSummoner();

        AsyncRequest requestRealm = apiAsync.getDataRealm(Platform.NA);
        requestRealm.addListeners(new RequestAdapter() {
            @Override
            public void onRequestSucceeded(AsyncRequest request) {
                eSummoner.relm = request.getDto();
            }
        });
        requestRealm.await();
        System.out.println(eSummoner.relm.getDd());
    }

}
