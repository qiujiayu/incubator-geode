/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.vmware.gemfire.tools.pulse.tests;

import javax.management.openmbean.TabularData;

public interface ServerObjectMBean {
  String OBJECT_NAME = "GemFire:service=System,type=Distributed";

  TabularData viewRemoteClusterStatus();

  int getMemberCount();

  int getNumClients();

  int getDistributedSystemId();

  int getLocatorCount();

  int getTotalRegionCount();

  int getNumRunningFunctions();

  long getRegisteredCQCount();

  int getNumSubscriptions();

  int getTransactionCommitted();

  int getTransactionRolledBack();

  long getTotalHeapSize();

  long getUsedHeapSize();

  long getMaxMemory();

  long getUsedMemory();

  long getTotalRegionEntryCount();

  int getCurrentQueryCount();

  long getTotalDiskUsage();

  float getDiskWritesRate();

  float getAverageWrites();

  float getAverageReads();

  float getQueryRequestRate();

  float getDiskReadsRate();

  long getJVMPauses();

  String[] listCacheServers();

  String[] listServers();

  String queryData(String p0, String p1, int p2);
}
