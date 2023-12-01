//
//  EditServiceView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//

import SwiftUI

struct EditServiceView: View {
    @State var service: Service
    var onServiceUpdated: () -> Void

    var body: some View {
        Form {
            TextField("Название услуги", text: $service.name)
            TextField("Стоимость для отечественных", text: Binding(
                get: { String(service.costOur) },
                set: { service.costOur = Double($0) ?? service.costOur }
            ))
            TextField("Стоимость для импортных", text: Binding(
                get: { String(service.costForeign) },
                set: { service.costForeign = Double($0) ?? service.costForeign }
            ))
            Button("Обновить") {
                updateService()
            }
        }
        .navigationTitle("Редактировать Услугу")
    }

    private func updateService() {
        guard let serviceId = service.id else {
            print("Service ID is missing")
            return
        }

        ServiceNetworkManager.shared.updateService(serviceId: serviceId, updatedService: service) { success in
            if success {
                print("Услуга успешно обновлена")
                onServiceUpdated() // Обновление списка услуг в родительском виде
            } else {
                print("Ошибка при обновлении услуги")
            }
        }
    }
}

