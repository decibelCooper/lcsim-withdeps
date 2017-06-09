/*
 * CylindricalBarrelCalorimeterTest.java
 *
 * Created on June 15, 2005, 12:00 PM
 */

package org.lcsim.geometry.subdetector;

import hep.graphics.heprep.HepRepProvider;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jdom.JDOMException;
import org.lcsim.geometry.Calorimeter;
import org.lcsim.geometry.GeometryReader;
import org.lcsim.geometry.compact.Detector;
import org.lcsim.util.xml.ElementFactory.ElementCreationException;

import org.lcsim.geometry.layer.Layering;

/**
 *
 * @author jeremym
 */
public class EcalBarrelTest extends TestCase
{
    Detector detector;      
    
    public static junit.framework.Test suite()
    {
        return new TestSuite(EcalBarrelTest.class);
    }    
    
    protected void setUp() throws java.lang.Exception
    {
        InputStream in = this.getClass().getResourceAsStream("EcalBarrelTest.xml");        
        GeometryReader reader = new GeometryReader();
        Detector det = reader.read(in);        
    }
    
    public void testIt()
    {}    
}
