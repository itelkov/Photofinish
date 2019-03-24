package ru.selket.photofinish.api.photo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhotoImageService {

    @Autowired
    private PhotoImageRepository photoImageRepository;

    public void set(PhotoImage photoImage) {
        photoImageRepository.save(photoImage);
    }

    public void delete(PhotoImage photoImage) {
        photoImageRepository.delete(photoImage);
    }

    public PhotoImage get(String id) {
        return photoImageRepository.findById(id).orElse(null);
    }
}
