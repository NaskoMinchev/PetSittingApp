package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.entity.Image;
import bg.project.petsittingapp.repository.ImageRepository;
import bg.project.petsittingapp.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private static final String IMAGES_SAVE_PATH = "src/main/resources/static/images/";
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image createImage(MultipartFile pictureFile, String filePath) {

        try {
            File file = new File(IMAGES_SAVE_PATH + filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(pictureFile.getBytes());

            Image image = new Image();
            image.setImageUrl(filePath);

            imageRepository.save(image);

            return image;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFilePath(String folderName, String name, MultipartFile picture) {
        String[] splitPictureNameArgs = picture.getOriginalFilename().split("\\.");
        String pictureFileExt = splitPictureNameArgs[splitPictureNameArgs.length - 1];
        String pathPattern = "%s%s_%s.%s";
        return String.format(pathPattern, folderName,transformTitle(name), UUID.randomUUID(), pictureFileExt);
    }

    public String transformTitle(String title) {
        return title.toLowerCase().replaceAll("\\s+", "_");
    }

    @Override
    public void deleteUnusedArticleImages(List<Long> articleImageIds, List<Long> petImageIds) {

        List<Image> images = imageRepository.findAll();

        for (Image image : images) {
            if (!articleImageIds.contains(image.getId()) && !petImageIds.contains(image.getId())) {
                imageRepository.deleteById(image.getId());

                try {
                    Files.delete(Path.of(IMAGES_SAVE_PATH + image.getImageUrl()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
