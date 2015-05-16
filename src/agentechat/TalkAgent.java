/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentechat;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.Dimension;
import java.awt.MultipleGradientPaint;
import java.awt.Toolkit;

/**
 *
 * @author miller.barrera
 */
public class TalkAgent extends GuiAgent {

    private ChatRoom chatRoom;
    private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

    @Override
    protected void setup() {

        chatRoom = new ChatRoom(this);

        chatRoom.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = chatRoom.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        chatRoom.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);;
        chatRoom.setVisible(true);

        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                ACLMessage reply = receive();
                if (reply != null) {
                    String name = getAID().getName();
                    String local = getAID().getLocalName();
                    String content = reply.getContent();
                    String sender = reply.getSender().getName();
                    chatRoom.taReceivedMessages.append("\n" + sender + ": " + content);

                    if (sender.equalsIgnoreCase("miller@192.168.0.15:1099/JADE")) {

                        createReply(reply);
                    }

                } else {
                    block();
                } //To change body of generated methods, choose Tools | Templates.
            }
        });

    }

    public void createReply(ACLMessage reply) {       
        ACLMessage response = reply.createReply();
        response.addReceiver(reply.getSender());
        response.setContent("Quiubooo  que quiere " + reply.getSender().getLocalName());
        send(response);
       

    }

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        String receiverName = (String) ge.getParameter(0);
        String msgContent = (String) ge.getParameter(1);
        ACLMessage toSend = new ACLMessage(ACLMessage.INFORM);
        toSend.setContent(msgContent);
        toSend.setPerformative(ACLMessage.INFORM);
        toSend.addReceiver(new AID(receiverName, AID.ISGUID));
        send(toSend);
    }

//    public class TalkBehaviour extends CyclicBehaviour{
//
//    public TalkBehaviour(GuiAgent ga) {
//        super(ga);
//    }
//    
//    
//
//    @Override
//    public void action() {
//        ACLMessage reply = myAgent.receive(mt);
//        if(reply != null){
//            String content = reply.getContent();
//            String sender = reply.getSender().getName();
//            chatRoom.taReceivedMessages.append("\n" + sender + ": " + content);
//        }else{
//            block();
//        }
//    }
}
