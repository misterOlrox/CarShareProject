package com.olrox.image;

import com.olrox.car.ejb.ModelsManager;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Utils;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;

@Named
@ApplicationScoped
public class ModelRenderer {
    @EJB
    ModelsManager modelsManager;

    public byte[] render(long id) {
        byte[] image = modelsManager.getImage(id);
        if(image!=null){
            return image;
        }
        else {
            try {
                return Utils.toByteArray(Faces.getResourceAsStream("/resources/images/no-image-car.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
