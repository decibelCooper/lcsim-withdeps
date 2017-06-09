package org.lcsim.util.aida;

import hep.aida.IAnalysisFactory;
import hep.aida.IBaseHistogram;
import hep.aida.IDataPointSet;
import hep.aida.IFunction;
import hep.aida.IManagedObject;
import hep.aida.IPlottable;
import hep.aida.IPlotter;
import hep.aida.IPlotterRegion;
import hep.aida.ITree;

import java.io.File;
import java.io.IOException;

/**
 * This class will write all the plots from an AIDA file to a directory tree
 * on the file system, in a user-specified graphics format.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class GraphicsWriter {

    ITree tree;
    String graphicsFormat = "png";
    File outputDir = new File(".");
    IPlotter plotter;

    /**
     * Class constructor.
     * @param aidaFile The input AIDA file.
     * @param graphicsFormat The graphics format e.g. "png", etc.
     * @param outputDir The base output directory, which is created if it does not exist.
     */
    public GraphicsWriter(File aidaFile, String graphicsFormat, File outputDir) {
        IAnalysisFactory analysisFactory = IAnalysisFactory.create();
        if (!aidaFile.exists())
            throw new IllegalArgumentException("The input AIDA file does not exist.");
        try {
            tree = analysisFactory.createTreeFactory().create(aidaFile.getAbsolutePath());
        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException(e);
        }
        plotter = analysisFactory.createPlotterFactory().create();
        if (graphicsFormat != null)
            this.graphicsFormat = graphicsFormat;
        if (outputDir != null) {
            this.outputDir = outputDir;
        }
    }
    
    /**
     * Class constructor.
     * @param tree An existing AIDA ITree that is already in memory.
     * @param graphicsFormat The graphics format e.g. "png" etc.
     * @param outputDir The base output directory, which is created if it does not exist.
     */
    public GraphicsWriter(ITree tree, String graphicsFormat, File outputDir) {
        if (tree != null)
            this.tree = tree;
        else
            throw new IllegalArgumentException("The tree points to null.");
        plotter = IAnalysisFactory.create().createPlotterFactory().create();
        if (graphicsFormat != null)
            this.graphicsFormat = graphicsFormat;
        if (outputDir != null)
            this.outputDir = outputDir;
    }

    /**
     * Write the AIDA objects from the current ITree to a hierarchy of directories.
     */
    public void writeToDir() {
        File absDir = outputDir.getAbsoluteFile();
        for (String path : tree.listObjectNames()) {
            String[] objectNames = tree.listObjectNames(path);
            for (String objectName : objectNames) {
                IManagedObject object = tree.find(objectName);                
                plotter.createRegion();
                IPlotterRegion region = plotter.region(0);
                if (object instanceof IBaseHistogram)
                    region.plot((IBaseHistogram) object);
                else if (object instanceof IDataPointSet)
                    region.plot((IDataPointSet) object);
                else if (object instanceof IFunction)
                    region.plot((IFunction) object);
                else if (object instanceof IPlottable)
                    region.plot((IPlottable) object);

                File plotFile = new File(absDir + File.separator + objectName + "." + graphicsFormat);
                File plotDir = plotFile.getParentFile();
                if (!plotDir.exists())
                    plotDir.mkdirs();
                
                try {
                    plotter.writeToFile(plotFile.getAbsolutePath(), graphicsFormat);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                plotter.destroyRegions();
            }
        }
    }
}