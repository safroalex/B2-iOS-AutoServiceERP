//
//  AddMasterView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 26.11.2023.
//
import SwiftUI
import Foundation

struct AddMasterView: View {
    @Binding var masters: [Master]
    @State private var name = ""
    @Environment(\.presentationMode) var presentationMode

    var body: some View {
        NavigationView {
            Form {
                TextField("Имя мастера", text: $name)
                Button("Добавить") {
                    addMaster()
                }
            }
            .navigationTitle("Новый мастер")
        }
    }

    func addMaster() {
        let newMaster = Master(name: name)
        NetworkManager.shared.addMaster(master: newMaster) { success in
            if success {
                masters.append(newMaster)
                presentationMode.wrappedValue.dismiss()
            } else {
                // Обработка ошибок
            }
        }
    }
}
