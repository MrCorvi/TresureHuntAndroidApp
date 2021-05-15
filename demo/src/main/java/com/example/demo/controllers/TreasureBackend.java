package com.example.demo.controllers;

import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.example.demo.models.*;

import org.json.simple.*;

import java.util.List;

@RestController
public class TreasureBackend {
 
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("jpatest");
    private int max_id = -1;

    @GetMapping("/test")
    public String testing(){
        String returnMessage = "";

		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction et = null;
    
        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();
    
            TreasureHuntStep p = new TreasureHuntStep(Integer.valueOf(1), "giammi", "q","a",Boolean.valueOf(true));
            p.setGameId(Integer.valueOf(100));
            //Prova p = new Prova("zz","y",Integer.valueOf(80));

            // Save the treasure hunt object
            em.persist(p);
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            // Close EntityManager
            em.close();
        }
        return returnMessage;
    }

    @GetMapping("/game")
    //public String getTreasureHunt(@RequestParam int gameId){
    public JSONObject getTreasureHunt(@RequestParam int gameId){

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        JSONObject response = new JSONObject();
        String returnMessage = "";

    	// the lowercase c refers to the object
    	// :gameId is a parameterized query thats value is set below
    	String query = "SELECT t FROM TreasureHuntStep t WHERE gameID= :gameId";
    	
    	// create a query using someJPQL, and this query will return instances of the TreasureHunt entity.
    	TypedQuery<TreasureHuntStep> tq = em.createQuery(query, TreasureHuntStep.class).setParameter("gameId", gameId);; 

    	List<TreasureHuntStep> th=null;
    	try {
    		th = tq.getResultList();
    		System.out.println(th.toString());
    	}
    	catch(NoResultException ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		em.close();
    	}
        
        JSONArray ja = new JSONArray();
        for(int i=0; i<th.size(); i++){
            //returnMessage += th.get(i).toString();
            //if(i<th.size()-1)
            //    returnMessage += "";
            ja.add(th.get(i).toJSON());
        }
        //returnMessage += "";
        response.put("game",ja);
        //return returnMessage;
        return response;
    }

    @GetMapping("/games")
    //public String getTreasureHunt(@RequestParam String initName){
    public JSONObject getTreasureHunt(@RequestParam String initName){

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String returnMessage = "";
        JSONObject response = new JSONObject();

    	// the lowercase c refers to the object
    	//String query = "SELECT t FROM TreasureHuntStep t WHERE treasureHuntName like :initName";
        String query = "SELECT new com.example.demo.models.customObject(t.gameID, t.gameName, COUNT(t.step)) FROM TreasureHuntStep t WHERE gameName like :initName GROUP BY t.gameName";

    	// create a query using someJPQL, and this query will return instances of the TreasureHunt entity.
    	//TypedQuery<TreasureHuntStep> tq = em.createQuery(query, TreasureHuntStep.class).setParameter("initName", "%" + initName + "%"); 
        TypedQuery<customObject> tq = em.createQuery(query, customObject.class).setParameter("initName", "%" + initName + "%"); 

    	//List<TreasureHuntStep> th=null;
        List<customObject> th=null;
    	try {
    		th = tq.getResultList();
    		System.out.println(th.toString());
    	}
    	catch(NoResultException ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		em.close();
    	}
        
        JSONArray ja = new JSONArray();
        for(int i=0; i<th.size(); i++){
            //returnMessage += th.get(i).toString();
            //if(i<th.size()-1)
            //    returnMessage += ",";
            ja.add(th.get(i).toJSON());
        }
        //returnMessage += "]}";
        response.put("games",ja);

        return response;
        ////return returnMessage;
    }

    @RequestMapping(method=RequestMethod.POST, value="/game")
    public JSONObject createTreasureHunt(@RequestBody TreasureHunt g){

        String returnMessage="";
        JSONObject response = new JSONObject();
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction et = null;
        
        // set gameID
        if(max_id==-1){
            Query query = em.createQuery("SELECT MAX(t.gameID) FROM TreasureHuntStep t");
            max_id = (int) query.getSingleResult();
        }
        max_id=max_id+1;

        for(TreasureHuntStep tmp : g.getGame()){
            tmp.setGameId(max_id);
        }

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();
    
            for(int i=0; i<g.getGame().size(); i++){
                
                TreasureHuntStep th = (TreasureHuntStep) g.getGame().get(i);
                em.persist(th);   // Save the customer object
            }
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            // Close EntityManager
            em.close();
        }
        response.put("gameId",max_id);
        return response;
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/step")
    public static void deleteTreasureHuntStep(@RequestParam int gameId,@RequestParam int step) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        List<TreasureHuntStep> th=null;
 
        // When you write HQL (or JPQL) queries, you use the names of the types, not the tables.
        // es. HQL (Hibernate Query Language) doesn't support * notation
    	String query = "SELECT t FROM TreasureHuntStep t WHERE gameID= :gameId ORDER BY step ASC";
    	
    	// create a query using someJPQL, and this query will return instances of the TreasureHunt entity.
    	TypedQuery<TreasureHuntStep> tq = em.createQuery(query, TreasureHuntStep.class).setParameter("gameId", gameId);  	
    	try {
    		th = tq.getResultList();
    	}
    	catch(NoResultException ex) {
    		ex.printStackTrace();
        } finally {
            em.close();
        }

        th.remove(step-1);
        for(int i=1; i<=th.size(); i++)
            th.get(i-1).setStep(i);
        deleteTreasureHunt(gameId);

        try{
            em = ENTITY_MANAGER_FACTORY.createEntityManager();
            et = em.getTransaction();
            et.begin();

            for(int i=0; i<th.size(); i++){
                TreasureHuntStep ths = th.get(i);
                System.out.println(ths.toString());
                em.persist(ths);
            } 
            et.commit(); //commit a transaction only once
        }catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
        /*try {

            et = em.getTransaction();
            et.begin();
            th = em.createQuery( "SELECT t FROM TreasureHuntStep t WHERE gameid= :gameid AND step= :step", TreasureHuntStep.class).
                setParameter("gameid", gameId).setParameter("step",step).getSingleResult();
            em.remove(th);
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            // Close EntityManager
            em.close();
        }*/
    }

    // a resource is uniquely identified by its PATH (and not by its params)
    @RequestMapping(method=RequestMethod.DELETE, value="/game")
    public static void deleteTreasureHunt(@RequestParam int gameId) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        
 
        try {

            et = em.getTransaction();
            et.begin();
            List<TreasureHuntStep> g = em.createQuery( "SELECT t FROM TreasureHuntStep t WHERE gameid= :gameid", TreasureHuntStep.class).
                setParameter("gameid", gameId).getResultList();

            for(int i=0; i<g.size(); i++){
            
                TreasureHuntStep th = (TreasureHuntStep) g.get(i);
                em.remove(th);   // remove the treasurehuntstep object
                                 // better use transaction than DELETE
                                 // however DELETE query is not typed
                                 // because only return affected rows
            }
            et.commit();

        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            // Close EntityManager
            em.close();
        }
    }

}
