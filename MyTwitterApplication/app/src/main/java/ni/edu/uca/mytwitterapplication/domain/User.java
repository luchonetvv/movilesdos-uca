package ni.edu.uca.mytwitterapplication.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Clase concreta para mapear los datos provenientes del resultado de la peticion.
 */
public class User {

    private final int id;
    @SerializedName("name")
    private final String fullName;
    @SerializedName("screen_name")
    private final String screenName;
    private final String location;
    private final String description;
    @SerializedName("followers_count")
    private final int followersCount;
    @SerializedName("friends_count")
    private final int friendsCount;
    @SerializedName("favourites_count")
    private final int favouritesCount;
    @SerializedName("profile_image_url")
    private final String profileImageUrl;

    public User(int id, String fullName, String screenName, String location, String description,
                int followersCount, int friendsCount, int favouritesCount, String profileImageUrl) {
        this.id = id;
        this.fullName = fullName;
        this.screenName = screenName;
        this.location = location;
        this.description = description;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
        this.favouritesCount = favouritesCount;
        this.profileImageUrl = profileImageUrl;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", followersCount=" + followersCount +
                ", friendsCount=" + friendsCount +
                ", favouritesCount=" + favouritesCount +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
