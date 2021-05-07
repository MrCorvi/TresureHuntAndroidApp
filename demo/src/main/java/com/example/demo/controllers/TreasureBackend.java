package com.example.demo.controllers;

import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.example.demo.models.*;

import java.util.List;

@RestController
public class TreasureBackend {
 
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("jpatest");

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
    
            TreasureHuntStep p = new TreasureHuntStep(Integer.valueOf(100), Integer.valueOf(1), "giammi", "q","a",Boolean.valueOf(true));
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
    public String getTreasureHunt(@RequestParam int gameId){

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String returnMessage = "";

    	// the lowercase c refers to the object
    	// :custID is a parameterized query thats value is set below
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
        
        for(int i=0; i<th.size(); i++)
            returnMessage += th.get(i).toString();

        return returnMessage;
    }

    @RequestMapping(method=RequestMethod.POST, value="/game")
    public String createTreasureHunt(@RequestBody TreasureHunt g){
        String returnMessage="";
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction et = null;
    
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
        return returnMessage;
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
            et.commit();
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
