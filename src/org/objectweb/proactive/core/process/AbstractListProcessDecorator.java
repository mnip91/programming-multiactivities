/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2002 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive-support@inria.fr
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://www.inria.fr/oasis/ProActive/contacts.html
 *  Contributor(s):
 *
 * ################################################################
 */
package org.objectweb.proactive.core.process;

import org.objectweb.proactive.core.util.MessageLogger;

import java.io.IOException;

import java.util.ArrayList;


/**
 * This class contains a list of ExternalProcessDecorator processes that share
 * the same configuration. It represents a wrapper on the list of processes.
 * @author ProActiveTeam
 * @version 1.0, 2 mars 2005
 * @since ProActive 2.2
 *
 */
public abstract class AbstractListProcessDecorator
    implements ExternalProcessDecorator {
    //Array of processes
    protected ArrayList processes;

    //baseProcess is used to get the common configuration
    protected ExternalProcessDecorator baseProcess;

    //represent the fixed part of the hostName
    protected String fixedName;
    protected String domain;
    protected String list;
    private String tmp;
    protected String beginIndex;
    protected String endIndex;
    protected int padding = 1;
    protected int step = 1;
    protected ArrayList excludeArray = new ArrayList();
    protected int nodeNumber = 0;

    public AbstractListProcessDecorator() {
        processes = new ArrayList();
    }

    public void setHostConfig(String fixedName, String list, String domain,
        int padding) {
        this.fixedName = fixedName;
        this.domain = domain;
        this.padding = padding;
        this.list = list.replaceAll("\\s", "");
        setAllIndex(this.list);  
        for (int i = Integer.parseInt(beginIndex);
                i <= Integer.parseInt(endIndex); i = i + step) {
            tmp = "" + i; //we change as String to check the array
            if (!excludeArray.contains(tmp)) {
                setHostname(handlePadding(tmp));
            }
        }
        baseProcess = (ExternalProcessDecorator) processes.get(0);
    }

    public void setHostList(String hostlistattr, String domain) {
        String[] hostlist = hostlistattr.split("\\s");
        if (checkNonEmpty(domain)) {
            if (!domain.startsWith(".")) {
                domain = "." + domain;
            }
            for (int i = 0; i < hostlist.length; i++) {
                String hostname = hostlist[i] + domain;
                setHostname(hostname);
            }
        } else {
            for (int i = 0; i < hostlist.length; i++) {
                setHostname(hostlist[i]);
            }
        }
        baseProcess = (ExternalProcessDecorator) processes.get(0);
    }

    //
    //--------------------------Implements ExternalProcessDecorator----------------------

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcessDecorator#getTargetProcess()
     */
    public ExternalProcess getTargetProcess() {
        return baseProcess.getTargetProcess();
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcessDecorator#setTargetProcess(org.objectweb.proactive.core.process.ExternalProcess)
     */
    public void setTargetProcess(ExternalProcess targetProcess) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setTargetProcess(targetProcess);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcessDecorator#getCompositionType()
     */
    public int getCompositionType() {
        return baseProcess.getCompositionType();
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcessDecorator#setCompositionType(int)
     */
    public void setCompositionType(int compositionType) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setCompositionType(compositionType);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcess#getInputMessageLogger()
     */
    public MessageLogger getInputMessageLogger() {
        return baseProcess.getInputMessageLogger();
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcess#getErrorMessageLogger()
     */
    public MessageLogger getErrorMessageLogger() {
        return baseProcess.getErrorMessageLogger();
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcess#getOutputMessageSink()
     */
    public MessageSink getOutputMessageSink() {
        return baseProcess.getOutputMessageSink();
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcess#setInputMessageLogger(org.objectweb.proactive.core.util.MessageLogger)
     */
    public void setInputMessageLogger(MessageLogger inputMessageLogger) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setInputMessageLogger(inputMessageLogger);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcess#setErrorMessageLogger(org.objectweb.proactive.core.util.MessageLogger)
     */
    public void setErrorMessageLogger(MessageLogger errorMessageLogger) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setErrorMessageLogger(errorMessageLogger);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.ExternalProcess#setOutputMessageSink(org.objectweb.proactive.core.process.MessageSink)
     */
    public void setOutputMessageSink(MessageSink outputMessageSink) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setOutputMessageSink(outputMessageSink);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getEnvironment()
     */
    public String[] getEnvironment() {
        return baseProcess.getEnvironment();
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#setEnvironment(java.lang.String[])
     */
    public void setEnvironment(String[] environment) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setEnvironment(environment);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getHostname()
     */
    public String getHostname() {
        return fixedName;
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#setHostname(java.lang.String)
     */
    public void setHostname(String hostname) {
        ExternalProcessDecorator process = createProcess();
        process.setHostname(hostname);
        processes.add(process);
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getUsername()
     */
    public String getUsername() {
        return baseProcess.getUsername();
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#setUsername(java.lang.String)
     */
    public void setUsername(String username) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setUsername(username);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getCommand()
     */
    public String getCommand() {
        return baseProcess.getCommandPath() + " " + fixedName + " " + list +
        " " + domain;
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getProcessId()
     */
    public String getProcessId() {
        return baseProcess.getProcessId();
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getNodeNumber()
     */
    public int getNodeNumber() {
        if (nodeNumber == 0) {
            for (int i = 0; i < processes.size(); i++) {
                nodeNumber = nodeNumber +
                    ((ExternalProcessDecorator) processes.get(i)).getNodeNumber();
            }
        }
        return nodeNumber;
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getFinalProcess()
     */
    public UniversalProcess getFinalProcess() {
        return baseProcess.getFinalProcess();
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#startProcess()
     */
    public void startProcess() throws IOException {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).startProcess();
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#stopProcess()
     */
    public void stopProcess() {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).stopProcess();
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#waitFor()
     */
    public int waitFor() throws InterruptedException {
        int status = 0;
        for (int i = 0; i < processes.size(); i++) {
            status = ((ExternalProcessDecorator) processes.get(i)).waitFor();
            if (status != 0) {
                return status;
            }
        }
        return status;
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#isStarted()
     */
    public boolean isStarted() {
        boolean started = true;
        for (int i = 0; i < processes.size(); i++) {
            started = ((ExternalProcessDecorator) processes.get(i)).isStarted();
            if (!started) {
                return started;
            }
        }
        return started;
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#isFinished()
     */
    public boolean isFinished() {
        boolean finished = true;
        for (int i = 0; i < processes.size(); i++) {
            finished = ((ExternalProcessDecorator) processes.get(i)).isFinished();
            if (!finished) {
                return finished;
            }
        }
        return finished;
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#setCommandPath(java.lang.String)
     */
    public void setCommandPath(String path) {
        for (int i = 0; i < processes.size(); i++) {
            ((ExternalProcessDecorator) processes.get(i)).setCommandPath(path);
        }
    }

    /**
     * @see org.objectweb.proactive.core.process.UniversalProcess#getCommandPath()
     */
    public String getCommandPath() {
        return baseProcess.getCommandPath();
    }

    //
    //-----------------protected methods----------------------------------------
    //
    protected abstract ExternalProcessDecorator createProcess();

    protected void setAllIndex(String list) {
        int index1 = list.indexOf(';');
        int index2 = list.indexOf(']');
        int index3 = list.indexOf('^');
        beginIndex = list.substring(1, list.indexOf('-'));
        if (index1 == -1) {
            endIndex = list.substring(list.indexOf('-') + 1, index2);
        } else {
            endIndex = list.substring(list.indexOf('-') + 1, index1);
            String scount = list.substring(index1 + 1, index2);
            step = new Integer(scount).intValue();
        }
        if (index3 != -1) {
            String[] listexclude = list.substring(list.lastIndexOf('[') + 1,
                    list.lastIndexOf(']')).split(",");
            for (int i = 0; i < listexclude.length; i++) {
                excludeArray.add(listexclude[i]);
            }
        }
    }

    protected String handlePadding(String word) {
        int wlength = word.length();
        if (wlength < padding) {
            for (int i = 0; i < (padding - wlength); i++) {
                word = "0" + word;
            }
        }
        if (!domain.startsWith(".")) {
            domain = "." + domain;
        }
        return fixedName + word + domain;
    }

    protected boolean checkNonEmpty(String s) {
        return (s != null) && (s.length() > 0);
    }
}
