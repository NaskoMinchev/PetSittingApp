package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image createImage(MultipartFile pictureFile, String filePath);
    String getFilePath(String folderName, String fileName, MultipartFile picture);
    void deleteUnusedArticleImages(List<Long> articleImageIds, List<Long> petImageIds);
}
