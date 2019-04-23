package com.olrox.image;

import com.olrox.car.domain.Model;
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
    private ModelsManager modelsManager;

    public byte[] render(long id) {
        Model model = modelsManager.find(id);
        byte[] image = model.getImage();
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
