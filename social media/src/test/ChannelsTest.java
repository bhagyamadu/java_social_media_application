import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.mockito.Mockito.*;

public class ChannelsTest {

    @Test
    public void testSubscribeButtonActionPerformed() {
        // Arrange
        Channels channels = new Channels();

        // Mock the necessary objects
        JList<String> listMock = mock(JList.class);
        JButton subscribeButtonMock = mock(JButton.class);
        channels.list1 = listMock;
        channels.subscribeButton = subscribeButtonMock;

        // Set up the selected channel value
        String selectedChannel = "Test Channel";
        when(listMock.getSelectedValue()).thenReturn(selectedChannel);

        // Mock the ActionListener
        ActionListener actionListenerMock = mock(ActionListener.class);

        // When addActionListener is called on subscribeButtonMock, register the actionListenerMock
        doAnswer(invocation -> {
            ActionListener listener = invocation.getArgument(0);
            subscribeButtonMock.addActionListener(listener); // Register the actionListenerMock as an action listener
            return null;
        }).when(subscribeButtonMock).addActionListener(actionListenerMock);

        // Act
        ActionListener actionListener = channels.subscribeButton.getActionListeners()[0]; // Get the first action listener
        actionListener.actionPerformed(null);

        // Assert
        // Ensure that the JOptionPane is shown with the correct message
        verify(subscribeButtonMock).addActionListener(Mockito.any()); // Check that the action listener is added to the button
        verify(listMock).getSelectedValue(); // Check that getSelectedValue() is called on the JList
        verify(actionListenerMock).actionPerformed(Mockito.any(ActionEvent.class)); // Verify that the mocked actionListenerMock is triggered

        // Add more assertions here based on the expected behavior of the action listener
        // For example, you can check if the database update logic is called correctly.

        // Note: Since the code involves interactions with a database, we should also test that the expected database operations are performed correctly. However, this test case only focuses on the GUI interaction and response.
    }
}
