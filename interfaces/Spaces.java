package interfaces;

import interfaces.Allclass.Space;

import java.util.List;

public interface Spaces {

    // Method to add a new space
    void addSpace(Space space) ;

    // Method to update an existing space
    void updateSpace(Space space) ;

    // Method to delete a space by its ID
    void deleteSpace(int spaceId) ;

    // Method to find a space by its ID
    Space findSpaceById(int spaceId) ;

    // Method to get all spaces
    List<Space> findAllSpacesByGestionnaire(int userID) ;

    // Method to find spaces by a specific feature
    List<Space> findSpacesByFeature(String feature) ;

    // Method to find spaces by availability
    List<Space> findAvailableSpaces() ;
}
