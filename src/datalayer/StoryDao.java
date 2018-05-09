package datalayer;

import models.StoryModel;
import models.UserModel;

import java.io.*;
import java.util.ArrayList;

public class StoryDao {

    /**
     * Given a story ID, return the story.
     */
    public static StoryModel getStory(int storyId) {
        File file = new File(getFilePath(storyId));
        return getStory(file);
    }

    /*
     * Given a story ID, delete it from storage.
     */
    public static void deleteStory(int storyId) {
        File file = new File(getFilePath(storyId));
        file.delete();
    }

    /*
     * Save the given story model.  Make sure you've set
     * the ID in the story model.
     */
    public static void saveStory(StoryModel storyModel){
        try {
            File file = new File(getFilePath(storyModel.getStoryId()));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storyModel);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a story ID and story text, make a story model
     * and save it.
     */
    public static void saveStory(int storyId, String storyText, String username, int commentOnStoryId) {
        StoryModel story = new StoryModel();
        story.setStoryId(UniqueIdDao.getID());
        story.setStory(storyText);
        story.setUsername(username);
        story.setCommentOnStoryID(commentOnStoryId);
        saveStory(story);
    }

    public static ArrayList<StoryModel> getStoriesByUser(UserModel user){
        ArrayList<StoryModel> allStories = StoryDao.getStories();
        ArrayList<StoryModel> userStories = new ArrayList<>();

        for (StoryModel story: allStories){
            if (story.getUsername().equalsIgnoreCase(user.getUsername())){
                userStories.add(story);
            }
        }
        return userStories;
    }

    /**
     * Return all saved stories.
     */
    public static ArrayList<StoryModel> getStories() {
        ArrayList<StoryModel> stories = new ArrayList<>();
        String dir = DaoUtils.storageDirectoryName();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++){
            if(listOfFiles[i].getName().startsWith("story") &&
                    listOfFiles[i].getName().endsWith(".txt")){
                stories.add(getStory(listOfFiles[i]));
            }
        }

        return stories;
    }

    public static ArrayList<StoryModel> getStoriesThatAreComments(int commentsOnStoryID){
        ArrayList<StoryModel> stories = new ArrayList<>();
        String dir = DaoUtils.storageDirectoryName();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++){
            if(listOfFiles[i].getName().startsWith("story") && listOfFiles[i].getName().endsWith(".txt")){
                StoryModel story = getStory(listOfFiles[i]);
                if(story.getCommentOnStoryID() == commentsOnStoryID){
                    stories.add(getStory(listOfFiles[i]));
                }
            }
        }

        return stories;
    }

    /**
     * Given a story ID, where are we saving it to storage (file name)?
     */
    private static String getFilePath(int storyId) {
        return DaoUtils.storageDirectoryName() + File.separator + "story" + storyId + ".txt";
    }

    /*
     * Given a story filename, return the story that's saved in the file.
     */
    private static StoryModel getStory(File file) {
        StoryModel story = null;
        try {
            story = new StoryModel();

            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                story = (StoryModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return story;
    }

    /**
     * Unit test program.
     *
     * @param args
     */
    public static void main(String[] args) {
        testStoryDao();
    }

    private static void testStoryDao() {
        int storyId = 100;
        String text = "It was a dark and stormy night.";
        StoryDao dao = new StoryDao();
        StoryModel story = new StoryModel();
        story.setStoryId(storyId);
        story.setStory("It was a dark and stormy night.");
        story.setUsername("allan");
        story.setCommentOnStoryID(0);
        dao.saveStory(story);

        story = dao.getStory(storyId);
        assert(story.getStoryId() == 100);
        assert(story.getStory().compareTo(text) == 0);

        ArrayList<StoryModel> stories = dao.getStories();
        assert(stories != null && stories.size() >= 5);

        //dao.deleteStory(storyId);
    }

}
