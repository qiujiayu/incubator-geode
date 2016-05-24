/*
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
 */
package com.gemstone.gemfire.internal.cache.partitioned;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import com.gemstone.gemfire.cache.CacheException;
import com.gemstone.gemfire.cache.query.QueryException;
import com.gemstone.gemfire.distributed.internal.DistributionManager;
import com.gemstone.gemfire.internal.cache.DataLocationException;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.internal.cache.PartitionedRegion;
import com.gemstone.gemfire.internal.cache.TXId;
import com.gemstone.gemfire.internal.cache.TXManagerImpl;
import com.gemstone.gemfire.internal.cache.TXStateProxy;
import com.gemstone.gemfire.internal.cache.TXStateProxyImpl;
import com.gemstone.gemfire.test.fake.Fakes;
import com.gemstone.gemfire.test.junit.categories.UnitTest;


@Category(UnitTest.class)
public class PartitionMessageTest {

  private GemFireCacheImpl cache;
  private PartitionMessage msg;
  private DistributionManager dm;
  private PartitionedRegion pr;
  private TXManagerImpl txMgr;
  private TXId txid;
  private long startTime = 1;
  TXStateProxy tx;
  
  @Before
  public void setUp() {
    cache = Fakes.cache();
    dm = mock(DistributionManager.class);  
    msg = mock(PartitionMessage.class);
    pr = mock(PartitionedRegion.class);
    txMgr = new TXManagerImpl(null, cache);
    tx = mock(TXStateProxyImpl.class);
    txid = new TXId(null, 0);
    
    when(msg.checkCacheClosing(dm)).thenReturn(false);
    when(msg.checkDSClosing(dm)).thenReturn(false);
    try {
      when(msg.getPartitionedRegion()).thenReturn(pr);
    } catch (PRLocallyDestroyedException e) {
      e.printStackTrace();
    }
    when(msg.getGemFireCacheImpl()).thenReturn(cache);
    when(msg.getStartPartitionMessageProcessingTime(pr)).thenReturn(startTime);
    when(msg.getTXManagerImpl(cache)).thenReturn(txMgr);
 
    doAnswer(new CallsRealMethods()).when(msg).process(dm);
    
  }

  @Test
  public void messageWithoutTxPerformsOnRegion() {   
    try {
      when(msg.masqueradeAs(msg, txMgr)).thenReturn(null);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    when(msg.hasTxAlreadyFinished(null, txMgr)).thenCallRealMethod(); 
    msg.process(dm);
    
    try {
      verify(msg, times(1)).operateOnPartitionedRegion(dm, pr, startTime);
    } catch (CacheException | QueryException | DataLocationException | InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void messageForUnFinishedTXPerformsOnRegion() {   
    try {
      when(msg.masqueradeAs(msg, txMgr)).thenReturn(tx);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    when(msg.hasTxAlreadyFinished(tx, txMgr)).thenCallRealMethod(); 
    msg.process(dm);
    
    try {
      verify(msg, times(1)).operateOnPartitionedRegion(dm, pr, startTime);
    } catch (CacheException | QueryException | DataLocationException | InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void messageForFinishedTXDoesNotPerformOnRegion() {   
    try {
      when(msg.masqueradeAs(msg, txMgr)).thenReturn(tx);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    when(msg.hasTXRecentlyCompleted(txid, txMgr)).thenReturn(true);
    when(msg.hasTxAlreadyFinished(tx, txMgr)).thenCallRealMethod(); 
    msg.process(dm);
    
    try {
      verify(msg, times(0)).operateOnPartitionedRegion(dm, pr, startTime);
    } catch (CacheException | QueryException | DataLocationException | InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }

}