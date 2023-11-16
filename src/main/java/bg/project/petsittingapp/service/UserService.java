package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;

public interface UserService {

    boolean register(UserRegisterBindingModel userRegisterBindingModel);

}
