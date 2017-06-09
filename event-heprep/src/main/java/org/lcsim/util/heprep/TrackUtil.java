package org.lcsim.util.heprep;

import java.util.List;

import org.lcsim.event.Track;
import org.lcsim.event.base.BaseTrack;
import org.lcsim.util.swim.HelixSwimmer;
import org.lcsim.util.swim.HelixSwimmerYField;

/**
 * A few utilities for display of Tracks in Wired.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class TrackUtil {
    
    private TrackUtil() {        
    }
    
    private static final double[] ORIGIN = { 0, 0, 0 };       
    private static final double[] YORIGIN = { 0, 0, 0.00001 }; /* FIXME: Hack for HPS!!! */
    
    /**
     * Get a <code>HelixSwimmer</code> implementation depending on the track type.
     * @param tracks The list of <code>Track</code> objects.
     * @param field The B-field components.
     * @return The <code>HelixSwimmer</code> implementation class.
     */
    static HelixSwimmer getHelixSwimmer(List<Track> tracks, double[] field) {
        HelixSwimmer swimmer = new HelixSwimmer(field[2]);
        if (tracks.size() > 0) {
            Track track = tracks.get(0);
            if (track.getType() == BaseTrack.TrackType.Y_FIELD.ordinal()) {
                swimmer = new HelixSwimmerYField(field[1]);
            } 
        }
        return swimmer;
    }
    
    /**
     * Get the origin coordinate to use depending on the track type.     
     * @param track A sample <code>Track</code> from the collection.
     * @return The origin as a double array containing X, Y, and Z coordinates.
     */
    // FIXME: This is basically a hack for HPS where the B-field is reported as zero at the origin
    // for most detector geometries.
    static double[] getOrigin(Track track) {
        if (track.getType() == BaseTrack.TrackType.Y_FIELD.ordinal())
            return YORIGIN;
        else 
            return ORIGIN;
    }
}