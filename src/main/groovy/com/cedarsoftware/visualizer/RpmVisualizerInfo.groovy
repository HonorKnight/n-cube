package com.cedarsoftware.visualizer

import com.cedarsoftware.ncube.ApplicationID
import com.cedarsoftware.ncube.NCube
import com.cedarsoftware.ncube.NCubeRuntimeClient
import groovy.transform.CompileStatic

import static com.cedarsoftware.visualizer.RpmVisualizerConstants.CUBE_TYPE_RPM
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.CUBE_TYPE
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.JSON_FILE_PREFIX
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.JSON_FILE_SUFFIX
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.TYPES_TO_ADD_CUBE_NAME
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.CONFIG_ITEM
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.CONFIG_ALL_TYPES
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.SOURCE_TYPE
import static com.cedarsoftware.visualizer.RpmVisualizerConstants.TARGET_TYPE

/**
 * Provides information to visualize rpm classes.
 */

@CompileStatic
class RpmVisualizerInfo extends VisualizerInfo
{
    //Set<Object> effectiveVersionAvailableValues = new LinkedHashSet()

    RpmVisualizerInfo(){}

    protected RpmVisualizerInfo(NCubeRuntimeClient runtimeClient, ApplicationID applicationID)
    {
        super(runtimeClient, applicationID)
    }

    @Override
    protected String getCubeType()
    {
        return CUBE_TYPE_RPM
    }

    @Override
    protected List getTypesToAdd(String group)
    {
        if (!group.endsWith(groupSuffix))
        {
            return typesToAddMap[allGroups[group]]
        }
        return null
    }

   @Override
   protected void loadTypesToAddMap(NCube configCube)
    {
        typesToAddMap = [:]
        NCube typesToAddCube = runtimeClient.getNCubeFromResource(appId, JSON_FILE_PREFIX + TYPES_TO_ADD_CUBE_NAME + JSON_FILE_SUFFIX)
        Set<String> allTypes = configCube.getCell([(CONFIG_ITEM): CONFIG_ALL_TYPES, (CUBE_TYPE): cubeType]) as Set

        allTypes.each { String sourceType ->
            Map<String, Boolean> map = typesToAddCube.getMap([(SOURCE_TYPE): sourceType, (TARGET_TYPE): new LinkedHashSet()]) as Map
            List<String> typesToAdd = map.findAll { String type, Boolean available ->
                available
            }.keySet() as List
            typesToAddMap[sourceType] = typesToAdd
        }
    }

    @Override
    protected String getNodeLabel()
    {
        'class'
    }

    @Override
    protected String getNodesLabel()
    {
        return 'classes'
    }

    protected String getCellValuesLabel()
    {
        return 'traits'
    }
}