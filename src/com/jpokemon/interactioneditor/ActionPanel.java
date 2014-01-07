package com.jpokemon.interactioneditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.interaction.ActionData;
import org.jpokemon.item.ItemInfo;
import org.jpokemon.item.Store;
import org.jpokemon.trainer.Event;

import com.jpokemon.util.FeedbackInputField;
import com.jpokemon.util.ui.selector.ActionTypeSelector;
import com.jpokemon.util.ui.selector.EventSelector;
import com.jpokemon.util.ui.selector.ItemSelector;
import com.jpokemon.util.ui.selector.StoreSelector;

public class ActionPanel extends JPanel {
  private ActionData actionData;
  private JPanel extraPanel = new JPanel();
  private ActionTypeSelector actionTypeSelector = new ActionTypeSelector();
  private boolean atomicOperationFlag = false;
  private EventSelector eventSelector;
  private ItemSelector itemSelector;
  private StoreSelector storeSelector;
  private FeedbackInputField extraTextField;

  private static final long serialVersionUID = 1L;

  public ActionPanel(ActionData a) {
    actionData = a;

    buildActionTypeSelector();
    add(new JLabel("#" + actionData.getId() + " in #" + actionData.getActionsetId()));
    add(actionTypeSelector);
    add(extraPanel);

    onActionTypeChange();
  }

  protected void buildActionTypeSelector() {
    actionTypeSelector.reload();
    actionTypeSelector.setSelectedItem(actionData.getAction());

    actionTypeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (atomicOperationFlag) {
          return;
        }
        onActionTypeChange();
      }
    });
  }

  protected void onActionTypeChange() {
    String action = (String) actionTypeSelector.getSelectedItem();

    actionData.setAction(action).commit();

    extraPanel.removeAll();
    if ("event".equals(action)) {
      buildEventAction();
    }
    else if ("item".equals(action)) {
      buildItemAction();
    }
    else if ("store".equals(action)) {
      buildStoreAction();
    }

    repaint();
  }

  protected void buildEventAction() {
    int eventId = 1;
    try {
      eventId = Integer.parseInt(actionData.getOptions());
    }
    catch (NumberFormatException e) {
      actionData.setOptions("1");
      actionData.commit();
    }

    if (eventSelector == null) {
      eventSelector = new EventSelector();
      eventSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          if (atomicOperationFlag) {
            return;
          }
          onEventChange();
        }
      });
    }

    atomicOperationFlag = true;
    eventSelector.reload();
    eventSelector.setSelectedItem(Event.get(eventId));
    atomicOperationFlag = false;

    extraPanel.add(eventSelector);
  }

  protected void onEventChange() {
    int eventId = ((Event) eventSelector.getSelectedItem()).getNumber();
    actionData.setOptions(eventId + "");
    actionData.commit();
  }

  protected void buildItemAction() {
    int itemId = 1, quantity = 1;
    try {
      String[] pieces = actionData.getOptions().split(" ");
      itemId = Integer.parseInt(pieces[0]);
      quantity = Integer.parseInt(pieces[1]);
    }
    catch (Exception e) {
      actionData.setOptions(itemId + " " + quantity);
      actionData.commit();
    }

    if (itemSelector == null) {
      itemSelector = new ItemSelector();
      itemSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          if (atomicOperationFlag) {
            return;
          }
          onItemChange();
        }
      });

      extraTextField = new FeedbackInputField();
      extraTextField.setPreferredSize(new Dimension(20, 20));
      extraTextField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onQuantityChange();
        }
      });
    }

    atomicOperationFlag = true;
    itemSelector.reload();
    itemSelector.setSelectedItem(ItemInfo.get(itemId));
    extraTextField.setSavedValue(quantity + "");
    extraTextField.setText(quantity + "");
    atomicOperationFlag = false;

    extraPanel.add(extraTextField);
    extraPanel.add(new JLabel("x"));
    extraPanel.add(itemSelector);
  }

  protected void onItemChange() {
    int itemId = ((ItemInfo) itemSelector.getSelectedItem()).getId();
    String quantity = extraTextField.getSavedValue();
    actionData.setOptions(itemId + " " + quantity);
    actionData.commit();
  }

  protected void onQuantityChange() {
    int quantity = 1;
    try {
      quantity = Integer.parseInt(extraTextField.getText());
    }
    catch (NumberFormatException e) {
    }

    extraTextField.setText(quantity + "");
    extraTextField.setSavedValue(quantity + "");

    int itemId = ((ItemInfo) itemSelector.getSelectedItem()).getId();
    actionData.setOptions(itemId + " " + quantity);
    actionData.commit();
  }

  protected void buildStoreAction() {
    int storeId = 1;
    try {
      storeId = Integer.parseInt(actionData.getOptions());
    }
    catch (NumberFormatException e) {
      actionData.setOptions("1");
      actionData.commit();
    }

    if (storeSelector == null) {
      storeSelector = new StoreSelector();
      storeSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          if (atomicOperationFlag) {
            return;
          }
          onStoreChange();
        }
      });
    }

    atomicOperationFlag = true;
    storeSelector.reload();
    storeSelector.setSelectedItem(Store.get(storeId));
    atomicOperationFlag = false;

    extraPanel.add(storeSelector);
  }

  protected void onStoreChange() {
    int storeId = ((Store) storeSelector.getCurrentElement()).getId();
    actionData.setOptions(storeId + "");
    actionData.commit();
  }
}