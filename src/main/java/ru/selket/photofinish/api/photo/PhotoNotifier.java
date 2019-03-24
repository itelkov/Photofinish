package ru.selket.photofinish.api.photo;

public interface PhotoNotifier {

    public void addNotice(PhotoRequest photoRequest, PhotoImage photoImage);
    public void removeNotice(PhotoRequest photoRequest, PhotoImage photoImage);
}
