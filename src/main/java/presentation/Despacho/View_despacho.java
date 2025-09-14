package presentation.Despacho;

import presentation.Logic.Receta;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View_despacho implements PropertyChangeListener {
    private JLabel searchIdLbl;
    private JTextField searchId;
    private JButton clear;
    private JButton search;
    private JTable tabla;
    private JButton verMedicamentosButton;
    private JPanel panel;
    private JButton entregarButton;
    private JButton avanzarEstadoButton;

    Controller controller;
    Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public View_despacho() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idBusqueda = searchId.getText().trim();
                controller.buscarReceta(idBusqueda);
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchId.setText("");
                controller.limpiarBusqueda();
            }
        });

        verMedicamentosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    Receta seleccionada = model.getList().get(fila);
                    JFrame window = new JFrame();
                    View_VerMedicamentos view = new View_VerMedicamentos();
                    Model modeloMedicamentos = new Model();
                    view.setModel(modeloMedicamentos);
                    modeloMedicamentos.setListaPrescripcion(seleccionada.getPrescripcions());
                    view.setObjeto(seleccionada.getPrescripcions());
                    window.setSize(600, 350);
                    window.setResizable(false);
                    window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    window.setTitle("Medicamentos de Receta: " + seleccionada.getIdReceta());
                    window.setLocationRelativeTo(null);
                    window.add(view.getPanel());

                    try {
                        modeloMedicamentos.setListmedicamentos();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al cargar medicamentos: " + ex.getMessage());
                    }
                    window.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione una receta primero.");
                }
            }
        });

        avanzarEstadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    Receta seleccionado = model.getList().get(fila);
                    try {
                        controller.modificarEstado(seleccionado);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al modificar estado: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione una receta primero.");
                }
            }
        });

        entregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    Receta seleccionado = model.getList().get(fila);
                    try {
                        controller.entregarReceta(seleccionado);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al entregar receta: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione una receta primero.");
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {DespachoTableModel.IDRECETA, DespachoTableModel.ESTADO,
                        DespachoTableModel.IDPACIENTE, DespachoTableModel.IDDOCTOR};
                tabla.setModel(new DespachoTableModel(cols, model.getList()));
                tabla.setRowHeight(30);
                break;
        }
    }

    public Component getPanel() {
        return panel;
    }
}