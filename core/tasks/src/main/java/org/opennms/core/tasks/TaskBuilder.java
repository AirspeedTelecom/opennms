/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2010 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 * OpenNMS Licensing       <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 */
package org.opennms.core.tasks;

/**
 * TaskBuilder
 *
 * @author brozow
 * @version $Id: $
 */
public class TaskBuilder<T extends ContainerTask<?>> {
    
    private T m_task;
    
    /**
     * <p>Constructor for TaskBuilder.</p>
     *
     * @param task a T object.
     * @param <T> a T object.
     */
    public TaskBuilder(T task) {
        m_task = task;
    }
    
    /**
     * <p>createSequence</p>
     *
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<SequenceTask> createSequence() {
        return m_task.getCoordinator().createSequence(m_task); 
    }
    
    /**
     * <p>createBatch</p>
     *
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<BatchTask> createBatch() {
        return m_task.getCoordinator().createBatch(m_task); 
    }
    
    /**
     * <p>setParent</p>
     *
     * @param parent a {@link org.opennms.core.tasks.ContainerTask} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> setParent(ContainerTask<?> parent) {
        parent.add(m_task);
        return this;
    }
    
    /**
     * <p>addSequence</p>
     *
     * @param runnables a {@link java.lang.Runnable} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> addSequence(Runnable... runnables) {
        createSequence().add(runnables).setParent(m_task);
        return this;
    }
    
    /**
     * <p>addSequence</p>
     *
     * @param runIns a {@link org.opennms.core.tasks.RunInBatch} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> addSequence(RunInBatch... runIns) {
        createSequence().add(runIns).setParent(m_task);
        return this;
    }
    
    /**
     * <p>addBatch</p>
     *
     * @param runnables a {@link java.lang.Runnable} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> addBatch(Runnable... runnables) {
        createBatch().add(runnables).setParent(m_task);
        return this;
    }

    /**
     * <p>addBatch</p>
     *
     * @param runIns a {@link org.opennms.core.tasks.RunInBatch} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> addBatch(RunInBatch... runIns) {
        createBatch().add(runIns).setParent(m_task);
        return this;
    }

    
    /**
     * <p>add</p>
     *
     * @param runnables a {@link java.lang.Runnable} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> add(Runnable... runnables) {
        for(Runnable r : runnables) {
            m_task.add(r);
        }
        return this;
    }
    
    /**
     * <p>add</p>
     *
     * @param runIns a {@link org.opennms.core.tasks.RunInBatch} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> add(RunInBatch... runIns) {
        for(final RunInBatch runIn : runIns) {
            final TaskBuilder<BatchTask> bldr = createBatch();
            bldr.add(new Runnable() {
                public void run() {
                    runIn.run(bldr.get());
                }
            }).setParent(m_task);
        }
        return this;
    }
    
    /**
     * <p>add</p>
     *
     * @param needers a {@link org.opennms.core.tasks.NeedsContainer} object.
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> add(NeedsContainer... needers) {
        for(final NeedsContainer needer : needers) {
            add(new Runnable() {
                public void run() {
                    needer.run(m_task);
                }
            });
        }
        return this;
    }
    
    /**
     * <p>get</p>
     *
     * @return a T object.
     */
    public T get() {
        return m_task;
    }

    /**
     * <p>get</p>
     *
     * @param parent a {@link org.opennms.core.tasks.ContainerTask} object.
     * @return a T object.
     */
    public T get(ContainerTask<?> parent) {
        return setParent(parent).get();
    }
    

}
