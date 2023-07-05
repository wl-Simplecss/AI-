package AIchat.webservice;

import AIchat.entity.Faq;
import AIchat.util.CustomResponse;
import com.google.gson.JsonObject;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.persistence.EntityManagerFactory;


@Path("/talk")
@Produces(MediaType.APPLICATION_JSON)
public class FaqService {
    private static final String PERSISTENCE_UNIT_NAME = "default";
    private static EntityManagerFactory factory;

    @GET
    public JsonObject talkWithAI(@DefaultValue(" ") @QueryParam("content") String keyword) {

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        String answer = null;
        try {
            em.getTransaction().begin();
            Faq faq = em.createQuery("SELECT f FROM Faq f WHERE f.question LIKE :keyword", Faq.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setMaxResults(1)
                    .getSingleResult();
            answer = faq.getAnswer();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        if(answer==null)answer="对不起我无法回答你的问题，请你尝试再问问别的问题！";


        JsonObject obj = new JsonObject();
        try{

            obj.addProperty("content", answer);
            return CustomResponse.success(obj);
        } catch (Exception e) {
            return CustomResponse.error(obj, e.getMessage());
        }
    }
}