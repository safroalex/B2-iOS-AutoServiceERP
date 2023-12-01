//
//  EditMasterView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 26.11.2023.
//
import SwiftUI
import Foundation

struct EditMasterView: View {
    @State var master: Master
    var onMasterUpdated: () -> Void

    var body: some View {
        Form {
            TextField("Имя мастера", text: $master.name)
            Button("Обновить") {
                updateMaster()
            }
        }
        .navigationTitle("Редактировать Мастера")
    }

    private func updateMaster() {
        guard let masterId = master.id else {
            print("Master ID is missing")
            return
        }

        MasterNetworkManager.shared.updateMaster(masterId: masterId, updatedMaster: master) { success in
            if success {
                print("Мастер успешно обновлен")
                onMasterUpdated() // Обновление списка мастеров в родительском виде
            } else {
                print("Ошибка при обновлении мастера")
            }
        }
    }
}
