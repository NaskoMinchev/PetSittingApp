package bg.project.petsittingapp.service.schedulers;

import bg.project.petsittingapp.service.ArticleService;
import bg.project.petsittingapp.service.ImageService;
import bg.project.petsittingapp.service.PetService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteUnusedImagesScheduler {
    private final ImageService imageService;
    private final ArticleService articleService;
    private final PetService petService;

    public DeleteUnusedImagesScheduler(ImageService imageService, ArticleService articleService, PetService petService) {
        this.imageService = imageService;
        this.articleService = articleService;
        this.petService = petService;
    }

    @Scheduled(cron = "0 11 16 * * */1")
    public void deleteUnusedImages() {
        List<Long> articleImageIds = articleService.getAllArticleImageIds();
        List<Long> petImageIds = petService.getAllPetImageIds();
        imageService.deleteUnusedArticleImages(articleImageIds, petImageIds);
        System.out.println("deleted");
    }
}
