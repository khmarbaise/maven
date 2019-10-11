package org.apache.maven.model.building;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.Model;
import org.apache.maven.xml.sax.filter.DependencyKey;

/**
 * 
 * @author Robert Scholte
 * @since 3.7.0
 */
@Named
@Singleton
public class DefaultModelCacheManager implements ModelCacheManager
{
    private final Map<Path, Model> modelCache = new ConcurrentHashMap<>();
    
    private final Map<DependencyKey, Model> depKeyModelCache = new ConcurrentHashMap<>();
    
    @Override
    public void put( Path p, Model m )
    {
        modelCache.put( p, m );
        
        String groupId = m.getGroupId();
        if ( groupId == null && m.getParent() != null )
        {
            groupId = m.getParent().getGroupId();
        }
        
        String artifactId = m.getArtifactId();
        depKeyModelCache.put( new DependencyKey( groupId, artifactId ), m );
    }

    @Override
    public Model get( Path p )
    {
        return modelCache.get( p );
    }

    @Override
    public Model get( DependencyKey k )
    {
        return depKeyModelCache.get( k );
    }

}
